package com.wavefront.labs.convert.converter.datadog.query.functions;

import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;

import java.util.List;

public class FilteringFunctions {

	public static String rollup(DatadogFunction function) {
		List<String> args = function.getArguments();
		if (args.size() == 2) {
			String agg = args.get(0).trim().equals("avg") ? "mean" : args.get(0).trim();
			return "align(" + args.get(1).trim() + "s, " + agg + ", " + function.getQuery() + ")";
		} else if (args.size() == 1) {
			return "align(" + args.get(0).trim() + "s, " + function.getQuery() + ")";
		} else {
			return "";
		}
	}
}
