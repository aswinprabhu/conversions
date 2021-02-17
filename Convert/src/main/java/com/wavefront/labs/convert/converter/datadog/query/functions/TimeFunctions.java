package com.wavefront.labs.convert.converter.datadog.query.functions;

import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;

public class TimeFunctions {

	public static String asRate(DatadogFunction function) {
		return "asRate(" + function.getQuery() + ")";
	}

	public static String hourBefore(DatadogFunction function) {
		return "lag(1h, " + function.getQuery() + ")";
	}

	public static String dayBefore(DatadogFunction function) {
		return "lag(1d, " + function.getQuery() + ")";
	}

	public static String weekBefore(DatadogFunction function) {
		return "lag(1w, " + function.getQuery() + ")";
	}

	public static String monthBefore(DatadogFunction function) {
		return "lag(30d, " + function.getQuery() + ")";
	}

	public static String perSecond(DatadogFunction function) {
		return "asRate(" + function.getQuery() + ")";
	}

	public static String perMinute(DatadogFunction function) {
		return "asRate(" + function.getQuery() + ") * 60";
	}

	public static String perHour(DatadogFunction function) {
		return "asRate(" + function.getQuery() + ") * 3600";
	}

	public static String diff(DatadogFunction function) {
		return "deriv(" + function.getQuery() + ")";
	}

}
