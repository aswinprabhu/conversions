package com.wavefront.labs.convert.converter.datadog.query.functions;

import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotSupported {
	private static final Logger logger = LogManager.getLogger(NotSupported.class);

	public static String allow(DatadogFunction function) {
		return function.getQuery();
	}

	public static String warning(DatadogFunction function) {
		logger.warn("function not supported: " + function.getName());
		return function.getQuery();
	}

	public static String notFound(DatadogFunction function) {
		logger.error("function not found: " + function.getName());
		return function.getQuery();
	}
}
