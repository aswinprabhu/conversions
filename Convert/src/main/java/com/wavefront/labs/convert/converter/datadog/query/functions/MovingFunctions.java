package com.wavefront.labs.convert.converter.datadog.query.functions;

import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;

public class MovingFunctions {

	public static String ewma3(DatadogFunction function) {
		return "wmavg(3m, " + function.getQuery() + ")";
	}

	public static String ewma5(DatadogFunction function) {
		return "wmavg(5m, " + function.getQuery() + ")";
	}

	public static String ewma10(DatadogFunction function) {
		return "wmavg(10m, " + function.getQuery() + ")";
	}

	public static String ewma20(DatadogFunction function) {
		return "wmavg(20m, " + function.getQuery() + ")";
	}

	public static String median3(DatadogFunction function) {
		return "mmedian(3m, " + function.getQuery() + ")";
	}

	public static String median5(DatadogFunction function) {
		return "mmedian(5m, " + function.getQuery() + ")";
	}

	public static String median7(DatadogFunction function) {
		return "mmedian(7m, " + function.getQuery() + ")";
	}

	public static String median9(DatadogFunction function) {
		return "mmedian(9m, " + function.getQuery() + ")";
	}
}

