package com.wavefront.labs.convert.converter.datadog;

import com.wavefront.labs.convert.Converter;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public abstract class AbstractDatadogConverter implements Converter {

	protected Properties properties;
	protected DatadogExpressionBuilder expressionBuilder;

	protected String getBaseApiUrl(String resource) {
		String apiKey = properties.getProperty("datadog.api.key");
		String applicationKey = properties.getProperty("datadog.application.key");

		String url = "https://api.datadoghq.com/api/v1/";
		url += resource;
		url += "?api_key=" + apiKey;
		url += "&application_key=" + applicationKey;

		return url;
	}

	@Override
	public void init(Properties properties) {
		this.properties = properties;
		String expressionBuilderClass = properties.getProperty("convert.expressionBuilder", "");
		if (!expressionBuilderClass.equals("")) {
			try {
				expressionBuilder = (DatadogExpressionBuilder) Class.forName(expressionBuilderClass).getConstructor().newInstance();
			} catch (ReflectiveOperationException e) {
				LogManager.getLogger(this.getClass()).error("Could not create instance of: " + expressionBuilderClass, e);
				expressionBuilder = new DatadogExpressionBuilder();
			}
		} else {
			expressionBuilder = new DatadogExpressionBuilder();
		}
		expressionBuilder.init(properties);
	}

	@Override
	public abstract void parse(Object data) throws IOException;

	@Override
	public abstract List<Object> convert();
}
