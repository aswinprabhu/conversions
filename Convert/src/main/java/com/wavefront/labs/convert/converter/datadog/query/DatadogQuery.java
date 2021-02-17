package com.wavefront.labs.convert.converter.datadog.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatadogQuery {

	private String query;

	private String aggregator;
	private String metric;
	private String numeral;
	private List<String> scopes;
	private List<String> groups;
	private List<DatadogFunction> functions;
	private String conditionOperator;
	private String conditionValue;
	private DatadogQuery subQuery = null;

	public DatadogQuery(String query) {
		this.query = query;
		scopes = new ArrayList();
		groups = new ArrayList();
		functions = new ArrayList();
		parseQuery();
	}

	private void parseQuery() {
		if (query.indexOf(".as_count()") > 0) {
			query = query.replace(".as_count()", "");
			functions.add(new DatadogFunction("as_count"));
		}

		if (query.indexOf(".as_rate()") > 0) {
			query = query.replace(".as_rate()", "");
			functions.add(new DatadogFunction("as_rate"));
		}

		parseFunction(0);
	}

	private int parseFunction(int start) {

		int nameEnd = 0, argStart = 0, argEnd = 0;

		int i = start;
		for (; i < query.length(); i++) {
			if (metric != null && query.charAt(i) == '.') {
				i = parseFunction(i + 1);
				argStart = i + 1;
				continue;
			}

			switch (query.charAt(i)) {
				case '(':
					if (start < i && getMatchingCharFor(i) < query.indexOf(':')) {
						// Handling for query in the below format, which is used in alert:
						// avg(last_5m):avg:aws.rds.cpuutilization{function:auth,env:prod} > 90
						aggregator = query.substring(start, i);
						break;
					} else {
						if (nameEnd == 0) {
							nameEnd = i;
							argStart = parseFunction(i + 1);
							i = argStart;
							break;
						}
					}

				case '{':
					i = parseMetric(start);
					if (query.length() > i + 1 && query.charAt(i + 1) == '.') {
						i++;
						start = i + 1;
						break;
					} else {
						return i + 1;
					}

				case ')':
					if (query.length() > (i+1) && query.charAt(i+1) == ':') {
						// Handling for query in the below format, which is used in alert:
						// avg(last_5m):avg:aws.rds.cpuutilization{function:auth,env:prod} > 90
						String subquery = query.substring(i+2, query.length());
						subQuery = new DatadogQuery(subquery);
						return i;
					} else {
						if (nameEnd == 0) {
							numeral = query.substring(start, i - 1);
							return start;
						} else {
							argEnd = i;
							functions.add(createDatadogFunction(start, nameEnd, argStart, argEnd));
							return ((i + 1) < query.length() && query.charAt(i + 1) == ',') ? (i + 1) : i;
						}
					}

				default:
					break;
			}
		}

		return query.length();

	}

	private int parseMetric(int pos) {

		int ind = query.indexOf(":", pos);
		if (ind > 0 && ind < query.indexOf("{", pos)) {
			aggregator = query.substring(pos, ind);
			pos = ind + 1;
		}

		ind = query.indexOf("{", pos);
		metric = query.substring(pos, ind);
		pos = ind + 1;

		ind = query.indexOf("}", pos);
		scopes = Arrays.asList(query.substring(pos, ind).split(","));
		pos = ind + 1;

		ind = query.indexOf("{", pos);
		if (ind > 0) {
			pos = ind + 1;
			ind = query.indexOf("}", pos);
			groups = Arrays.asList(query.substring(pos, ind).split(","));
			return ind;
		}

		return pos - 1;

	}

	private DatadogFunction createDatadogFunction(int start, int nameEnd, int argStart, int argEnd) {

		String name = query.substring(start, nameEnd).trim();

		if (argStart < argEnd - 1) {
			String argsStr = query.substring(argStart, argEnd).trim();
			if (!argsStr.equals("")) {
				if (argsStr.startsWith(",")) {
					argsStr = argsStr.substring(1);
				}
				List<String> args = Arrays.asList(argsStr.split(","));
				return new DatadogFunction(name, args);
			}
		}

		return new DatadogFunction(name);
	}

	private int getMatchingCharFor(int index) {
		int matchIndex = -1;

		char c = query.charAt(index);
		int n = 1;
		if (c == '(') {
			char mc = ')';
			for (int i=index+1; i<query.length(); i++) {
				if (query.charAt(i) == mc) { n--; }
				else if (query.charAt(i) == c) { n++; }
				if (n == 0) {
					matchIndex = i;
					break;
				}
			}
		}

		return matchIndex;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(aggregator + ":" + metric);

		ret.append("{").append(String.join(",", scopes)).append("}");

		if (groups.size() > 0) {
			ret.append(" by {").append(String.join(",", groups)).append("}");
		}

		if (functions.size() > 0) {
			ret.append(" -");
			for (DatadogFunction function : functions) {
				ret.append("-> ").append(function.getName()).append("(");
				if (function.getArguments().size() > 0) {
					ret.append(String.join(", ", function.getArguments()));
				}
				ret.append(") ");
			}
		}

		return ret.toString();
	}

	public String getQuery() {
		return query;
	}

	public String getAggregator() {
		return aggregator;
	}

	public String getNumeral() {
		return (subQuery != null) ? subQuery.numeral : numeral;
	}

	public String getMetric() {
		return (subQuery != null) ? subQuery.metric : metric;
	}

	public List<String> getScopes() {
		return scopes;
	}

	public List<String> getGroups() {
		return groups;
	}

	public List<DatadogFunction> getFunctions() {
		return functions;
	}

	public DatadogQuery getSubQuery() {
		return subQuery;
	}
}
