package com.wavefront.labs.convert.converter.datadog.query.functions;

import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;

public class MathFunctions {

	public static String abs(DatadogFunction function) {
		return "abs(" + function.getQuery() + ")";
	}

	public static String log(DatadogFunction function) {
		return "log(" + function.getQuery() + ")";
	}

	public static String log10(DatadogFunction function) {
		return "log10(" + function.getQuery() + ")";
	}

	public static String cumsum(DatadogFunction function) {
		return "integral(" + function.getQuery() + ")";
	}

	public static String integral(DatadogFunction function) {
		return "integral(deriv(" + function.getQuery() + ")";
	}


}
