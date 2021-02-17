package com.wavefront.labs.convert.converter.datadog.query.functions;

import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;

public class MissingDataFunctions {

	public static String fill(DatadogFunction function) {
		String arg = function.getArguments().get(0).trim();

		switch (arg) {
			case "null":
				return function.getQuery();

			case "zero":
				if (function.getArguments().size() > 1) {
					String limit = function.getArguments().get(1).trim() + "s";
					return "default(" + limit + ", 0, " + function.getQuery() + ")";
				} else {
					return "default(0, " + function.getQuery() + ")";
				}

			case "linear":
				return "interpolate(" + function.getQuery() + ")";

			case "last":
				if (function.getArguments().size() > 1) {
					String limit = function.getArguments().get(1).trim() + "s";
					return "last(" + limit + ", " + function.getQuery() + ")";
				} else {
					return "last(" + function.getQuery() + ")";
				}

			default:
				return function.getQuery();
		}

	}

	public static String countNonzero(DatadogFunction function) {

		return "count(if(" + function.getQuery() + " != 0, " + function.getQuery() + "))";

	}

	public static String countNotNull(DatadogFunction function) {

		return "count(" + function.getQuery() + ")";

	}
}
