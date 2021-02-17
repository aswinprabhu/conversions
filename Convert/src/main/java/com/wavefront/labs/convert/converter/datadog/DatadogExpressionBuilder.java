package com.wavefront.labs.convert.converter.datadog;

import com.wavefront.labs.convert.DefaultExpressionBuilder;
import com.wavefront.labs.convert.Utils.Tracker;
import com.wavefront.labs.convert.converter.datadog.models.DatadogTemplateVariable;
import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;
import com.wavefront.labs.convert.converter.datadog.query.DatadogQuery;
import com.wavefront.labs.convert.converter.datadog.query.Variable;
import com.wavefront.labs.convert.converter.datadog.query.functions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DatadogExpressionBuilder extends DefaultExpressionBuilder {

	public static final String QUERY_SEPARATOR = " \"\"\" "; //should never seen 3 consecutive double quotes in any query
	public static final String QUERY_SEPARATOR_SPLIT = " \\\"\\\"\\\" ";

	private static final Logger logger = LogManager.getLogger(DatadogExpressionBuilder.class);

	private static final Pattern expressionListPattern = Pattern.compile("\\{.*?\\}|(\\+|-|\\*|\\/|;)|((<=|<|>=|>)(?=\\s\\d+$))|(\\d+(?<=(<=|<|>=|>)?)$)");
	private static final Pattern topConveniencePattern = Pattern.compile("^(top|bottom)(5|10|15|20)_?(mean|min|max|last|area|l2norm|norm)?$");
	private static final Pattern operatorNumberPattern = Pattern.compile("(<=|<|>|>=)|(\\+|-|\\*|/)|(-?\\d+([.]\\d+)?)");

	private static final HashMap<String, Function<DatadogFunction, String>> functionMap = new HashMap();

	static {
		functionMap.put("avg", AggregationFunctions::avg);
		functionMap.put("sum", AggregationFunctions::sum);
		functionMap.put("min", AggregationFunctions::min);
		functionMap.put("max", AggregationFunctions::max);
		functionMap.put("count", AggregationFunctions::count);
		functionMap.put("first", AggregationFunctions::first);
		functionMap.put("last", AggregationFunctions::last);
		functionMap.put("as_rate", TimeFunctions::asRate);
		functionMap.put("as_count", NotSupported::allow);
		functionMap.put("abs", MathFunctions::abs);
		functionMap.put("log2", MathFunctions::log);
		functionMap.put("log10", MathFunctions::log10);
		functionMap.put("cumsum", MathFunctions::cumsum); //TODO: investigate
		functionMap.put("integral", MathFunctions::integral); //TODO: investigate
		functionMap.put("fill", MissingDataFunctions::fill); //TODO: linear with time?
		functionMap.put("hour_before", TimeFunctions::hourBefore);
		functionMap.put("day_before", TimeFunctions::dayBefore);
		functionMap.put("week_before", TimeFunctions::weekBefore);
		functionMap.put("month_before", TimeFunctions::monthBefore);
		functionMap.put("per_second", TimeFunctions::perSecond);
		functionMap.put("per_minute", TimeFunctions::perMinute);
		functionMap.put("per_hour", TimeFunctions::perHour);
		functionMap.put("dt", NotSupported::warning);
		functionMap.put("diff", TimeFunctions::diff);
		functionMap.put("forecast", PredictiveFunctions::forecast);
		functionMap.put("derivative", NotSupported::warning);
		functionMap.put("ewma_3", MovingFunctions::ewma3);
		functionMap.put("ewma_5", MovingFunctions::ewma5);
		functionMap.put("ewma_10", MovingFunctions::ewma10);
		functionMap.put("ewma_20", MovingFunctions::ewma20);
		functionMap.put("median_3", MovingFunctions::median3);
		functionMap.put("median_5", MovingFunctions::median5);
		functionMap.put("median_7", MovingFunctions::median7);
		functionMap.put("median_9", MovingFunctions::median9);
		functionMap.put("rollup", FilteringFunctions::rollup);
		functionMap.put("count_nonzero", MissingDataFunctions::countNonzero);
		functionMap.put("count_not_null", MissingDataFunctions::countNotNull);
		functionMap.put("top", RankingFunctions::top);
		functionMap.put("top_offset", RankingFunctions::topOffset);
		functionMap.put("TOP_CONVENIENCE", RankingFunctions::topConvenience);
		functionMap.put("robust_trend", NotSupported::warning);
		functionMap.put("trend_line", NotSupported::warning);
		functionMap.put("piecewise_constant", NotSupported::warning);
		functionMap.put("anomalies", NotSupported::warning);
		functionMap.put("outliers", NotSupported::warning);

		functionMap.put("NOT_FOUND", NotSupported::notFound);
	}

	private String underscoreReplace;
	private HashMap<String, com.wavefront.labs.convert.converter.datadog.query.Variable> variablesMap;
	private Set<String> dropTags;
	private Set<String> metricsSet;
	public static Set<String> allMetrics = new HashSet<String>();

	@Override
	public void init(Properties properties) {
		super.init(properties);

		underscoreReplace = properties.getProperty("datadog.underscoreReplace", ".");
		dropTags = Arrays.stream(properties.getProperty("datadog.dropTags", "").split(",")).collect(Collectors.toSet());
		variablesMap = new HashMap();
		metricsSet = new HashSet<String>();
	}

	@Override
	public String buildMetricName(String orig) {
		String metricName = buildName(orig, "metric");
		metricName = metricName.replaceAll("_", underscoreReplace);
		return super.buildMetricName(metricName);
	}

	@Override
	public String buildExpression(Object data) {

		String origQuery = data.toString().trim();
		if (origQuery.equals("")) {
			return "";
		}

		ArrayList<String> queryList = createQueryList(origQuery);
		StringBuilder ts = new StringBuilder();

		for (String query : queryList) {
			query = query.trim();
			if (operatorNumberPattern.matcher(query).matches()) {
				ts.append(" ").append(query).append(" ");
			} else if (query.equals(";")) {
				ts.append(QUERY_SEPARATOR);
			} else {
				DatadogQuery datadogQuery = new DatadogQuery(query);
				ts.append(convertDatadogQuery(datadogQuery));
				if (datadogQuery.getMetric() != null) {
					metricsSet.add(datadogQuery.getMetric());
					allMetrics.add(datadogQuery.getMetric());
				}
			}
		}

		return ts.toString();
	}

	private String convertDatadogQuery(DatadogQuery datadogQuery) {
		try {
			String query = makeMetricQuery(datadogQuery);
			query = makeAggregateQuery(datadogQuery, query);
			query = makeFunctionQuery(datadogQuery, query);
			return query;
		} catch (Exception e) {
			logger.error("Could not convert Datadog query: " + datadogQuery.getQuery(), e);
			return "";
		}
	}

	private String makeFunctionQuery(DatadogQuery datadogQuery, String query) {

		for (DatadogFunction function : datadogQuery.getFunctions()) {
			Function<DatadogFunction, String> convertFunction;
			if (functionMap.containsKey(function.getName())) {
				convertFunction = functionMap.get(function.getName());
			} else if (topConveniencePattern.matcher(function.getName()).matches()) {
				convertFunction = functionMap.get("TOP_CONVENIENCE");
			} else {
				convertFunction = functionMap.get("NOT_FOUND");
			}

			function.setQuery(query);
			query = convertFunction.apply(function);
		}

		return query;
	}

	private String makeAggregateQuery(DatadogQuery datadogQuery, String query) {

		String aggregator = datadogQuery.getAggregator();

		if (aggregator != null) {
			query = datadogQuery.getAggregator() + "(" + query;

			List<String> groups = datadogQuery.getGroups();
			if (groups != null && groups.size() > 0) {
				StringJoiner aggGroups = new StringJoiner(", ", ", ", "");
				for (String group : groups) {
					if (group.equals("host") || group.equals("name")) {
						aggGroups.add("sources");
					} else {
						if (!dropTags.contains(group)) {
							group = buildName(group, "tagName");
							aggGroups.add(group);
						}
					}
				}
				query = query + aggGroups;
			}

			query = query + ")";
		}

		return query;
	}

	private String makeMetricQuery(DatadogQuery datadogQuery) {

		String query = "";
		if (datadogQuery.getSubQuery() != null) {
			query = convertDatadogQuery(datadogQuery.getSubQuery());
		} else {
			query = datadogQuery.getNumeral();
			if (datadogQuery.getMetric() != null && !"".equals(datadogQuery.getMetric())) {
				query = "ts(\"" + buildMetricName(datadogQuery.getMetric()) + "\"";

				List<String> scopes = datadogQuery.getScopes();
				if (scopes != null && scopes.size() > 0) {

					if (!scopes.get(0).equals("*")) {
						StringJoiner filters = new StringJoiner(" and ", ", ", "");

						for (String scope : scopes) {
							boolean notFilter = false;
							String filterValue = null;

							if (scope.startsWith("!")) {
								scope = scope.substring(1);
								notFilter = true;
							}

							if (scope.startsWith("$")) {
								String name = scope.substring(1);
								if (variablesMap.containsKey(name)) {
									Variable variable = variablesMap.get(name);

									if (variable.isGeneric()) {
										filterValue = "${" + variable.getName() + "}";

									} else if (!dropTags.contains(variable.getTagName())) {
										filterValue = variable.getTagName() + "=\"${" + variable.getName() + "}\"";
										if (variable.getMetric() == null) {
											variable.setMetric(datadogQuery.getMetric());
										}
									}
								}
								Tracker.increment("Ignored Filters In Chart Count");

							} else {
								String[] scopeParts = scope.split(":");

								if (scopeParts.length > 1) {
									if (!dropTags.contains(scopeParts[0])) {
										String tagName = buildName(scopeParts[0], "tagName");
										String tagValue = buildName(scopeParts[1], "tagValue");
										if (datadogQuery.getMetric() != null &&
												(datadogQuery.getMetric().startsWith("mysql") ||
														(datadogQuery.getMetric().startsWith("system")))) {
											filterValue = "tag=\"" + tagName + ":" + tagValue + "\"";
										} else {
											filterValue = tagName + "=\"" + tagValue + "\"";
										}
									}
								} else if (!dropTags.contains(scope)) {
									String tagName = buildName(scope, "tagName");
									filterValue = "tag=\"" + tagName + "\"";
								}
							}

							if (filterValue != null) {
								if (notFilter) {
									filterValue = "not " + filterValue;
								}
								filters.add(filterValue);
							}
						}

						if (filters.length() > 2) {
							query = query + filters;
						}
					}
				}

				query = query + ")";
			}
		}

		return query;
	}

	private ArrayList<String> createQueryList(String expression) {
		ArrayList<String> expressionList = new ArrayList();

		Matcher matcher = expressionListPattern.matcher(expression);

		int lastPos = 0;
		boolean endOfQuery = false;
		while (matcher.find()) {
			if (matcher.group(1) != null) {
				expressionList.add(expression.substring(lastPos, matcher.start()).trim());
				expressionList.add(matcher.group(1));
				lastPos = matcher.end();
			}
			if (matcher.group(2) != null) {
				expressionList.add(expression.substring(lastPos, matcher.start()).trim());
				expressionList.add(matcher.group(2));
				lastPos = matcher.end();
				endOfQuery = true;
			}
			if (matcher.group(4) != null) {
				expressionList.add(matcher.group(4));
				lastPos = matcher.end();
				endOfQuery = true;
			}
		}
		if (!endOfQuery) {
			expressionList.add(expression.substring(lastPos).trim());
		}

		return expressionList;
	}

	public void initVariablesMap(List<DatadogTemplateVariable> templateVariables) {

		variablesMap = new HashMap();
		metricsSet = new HashSet<String>();

		if (templateVariables != null) {
			for (DatadogTemplateVariable templateVariable : templateVariables) {
				String name = templateVariable.getName();
				String prefix = templateVariable.getPrefix();
				String _default = templateVariable.get_default();
				if (_default == null) {
					_default = "";
				}

				if (!_default.startsWith(":")) {
					Variable variable = new Variable();
					variable.setName(name);

					if (prefix == null || prefix.equals("")) {
						variable.setGeneric(true);
						if (_default.equals("*")) {
							_default = "";
						}

					} else if (dropTags.contains(prefix)) {
						continue;

					} else {
						variable.setTagName(prefix);
					}

					variable.setValue(_default.replace(":", "="));

					variablesMap.put(name, variable);
				}
			}
		}
	}

	public HashMap<String, Variable> getVariablesMap() {
		return variablesMap;
	}

	public Set<String> getMetricsSet() {
		return metricsSet;
	}
}
