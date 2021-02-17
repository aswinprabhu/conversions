package com.wavefront.labs.convert.converter.datadog.query.functions;

import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;

public class AggregationFunctions {

	public static String avg(DatadogFunction function) {
		return "avg(" + function.getQuery() + ")";
	}

	public static String sum(DatadogFunction function) {
		return "sum(" + function.getQuery() + ")";
	}

	public static String min(DatadogFunction function) {
		return "min(" + function.getQuery() + ")";
	}

	public static String max(DatadogFunction function) {
		return "max(" + function.getQuery() + ")";
	}

	public static String count(DatadogFunction function) {
		return "count(" + function.getQuery() + ")";
	}

	public static String first(DatadogFunction function) {
		return "first(" + function.getQuery() + ")";
	}

	public static String last(DatadogFunction function) {
		return "last(" + function.getQuery() + ")";
	}

}
