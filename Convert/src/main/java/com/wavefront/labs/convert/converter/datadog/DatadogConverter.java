package com.wavefront.labs.convert.converter.datadog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatadogConverter extends AbstractDatadogConverter {
	private static final Logger logger = LogManager.getLogger(DatadogApiConverter.class);

	private List<AbstractDatadogConverter> converters;

	@Override
	public void init(Properties properties) {
		super.init(properties);
		converters = new ArrayList<>();
	}

	@Override
	public void parse(Object data) throws IOException {

		AbstractDatadogConverter converter = null;

		if (data == null) {
			converter = new DatadogApiConverter();
		} else {
			if (Boolean.parseBoolean(properties.getProperty("datadog.timeboard.enabled", "true"))) {
				converter = new DatadogTimeboardConverter();
			} else if (Boolean.parseBoolean(properties.getProperty("datadog.alertTarget.enabled", "true"))) {
				converter = new DatadogAlertTargetConverter();
			} else if (Boolean.parseBoolean(properties.getProperty("datadog.alert.enabled", "true"))) {
				converter = new DatadogAlertConverter();
			} else {
				logger.error("No resources are enabled for conversion!");
			}
		}

		if (converter != null) {
			converter.init(properties);
			converter.parse(data);

			converters.add(converter);
		}
	}

	@Override
	public List<Object> convert() {

		List<Object> models = new ArrayList<>();

		for (AbstractDatadogConverter converter : converters) {
			models.addAll(converter.convert());
		}

		return models;

	}
}
