package com.wavefront.labs.convert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wavefront.labs.convert.name.NameProcessor;
import com.wavefront.labs.convert.name.NameRule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

public class DefaultExpressionBuilder implements ExpressionBuilder {
	private static final Logger logger = LogManager.getLogger(DefaultExpressionBuilder.class);

	protected Properties properties;

	private NameProcessor nameProcessor;

	@Override
	public void init(Properties properties) {
		this.properties = properties;

		try {
			String nameProcessorFile = properties.getProperty("convert.name.processor.file", "");
			if (!nameProcessorFile.equals("")) {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
				nameProcessor = mapper.readValue(new File(nameProcessorFile), new TypeReference<NameProcessor>() {
				});
			} else {
				nameProcessor = new NameProcessor();
			}
		} catch (IOException e) {
			logger.error("Error reading name processor file.", e);
			nameProcessor = new NameProcessor();
		}
	}

	@Override
	public String buildMetricName(String orig) {
		return orig.replaceAll("[^a-zA-Z0-9_,./\\-]", "-");
	}

	@Override
	public String buildExpression(Object data) {
		String name = buildName(data.toString());
		String metricName = buildMetricName(name);
		return "ts(\"" + metricName + "\")";
	}

	protected String buildName(String name) {
		return buildName(name, null);
	}

	protected String buildName(String name, String type) {
		if (type == null || type.equals("")) {
			type = "metric";
		}

		HashMap<String, String> nameMap = nameProcessor.getMap(type);
		if (nameMap.containsKey(name)) {
			name = nameMap.get(name);

		} else {
			List<NameRule> rules = nameProcessor.getRules(type);
			for (NameRule rule : rules) {
				if (rule.getMatchPattern().matcher(name).matches()) {
					Matcher matcher = rule.getSearchPattern().matcher(name);
					if (matcher.find()) {
						name = matcher.replaceAll(rule.getReplace());
					}
				}
			}
		}

		return name;
	}
}
