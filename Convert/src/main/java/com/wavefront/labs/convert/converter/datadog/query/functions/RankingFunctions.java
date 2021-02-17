package com.wavefront.labs.convert.converter.datadog.query.functions;

import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class RankingFunctions {
	private static final Logger logger = LogManager.getLogger(RankingFunctions.class);

	public static String top(DatadogFunction function) {
		int num = Integer.parseInt(function.getArguments().get(0).trim());
		String aggr = function.getArguments().get(1).trim();
		boolean isDescending = function.getArguments().get(2).trim().equals("'desc'");
		String funcName = isDescending ? "top" : "bottom";

		switch (aggr) {
			case "'last'":
				return funcName + "k(" + num + ", " + function.getQuery() + ")";
			case "'mean'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + funcName + "(" + num + ", mavg(1h, " + function.getQuery() + ")), " + function.getQuery() + ")";
			case "'min'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + funcName + "(" + num + ", mmin(1h, " + function.getQuery() + ")), " + function.getQuery() + ")";
			case "'max'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + funcName + "(" + num + ", mmax(1h, " + function.getQuery() + ")), " + function.getQuery() + ")";
            case "'sum'":
			case "'area'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + funcName + "(" + num + ", msum(1h, " + function.getQuery() + ")), " + function.getQuery() + ")";
			case "'l2norm'":
			case "'norm'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + funcName + "(" + num + ", msum(1h, pow(" + function.getQuery() + ", 2))), " + function.getQuery() + ")";
			default:
				break;
		}

		logger.error("unsupported aggregate for top: " + aggr);
		return function.getQuery();
	}

	public static String topOffset(DatadogFunction function) {
		int num = Integer.parseInt(function.getArguments().get(0).trim());
		String aggr = function.getArguments().get(1).trim();
		boolean isDescending = function.getArguments().get(2).trim().equals("'desc'");
		String funcName = isDescending ? "top" : "bottom";
		String flipFuncName = isDescending ? "bottom" : "top";
		int offset = Integer.parseInt(function.getArguments().get(3).trim());

		switch (aggr) {
			case "'last'":
				return flipFuncName + "k(" + num + ", " + funcName + "k(" + (num + offset) + ", " + function.getQuery() + "))";
			case "'mean'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + flipFuncName + "(" + num + ", " + funcName + "(" + (num + offset) + ", mavg(1h, " + function.getQuery() + ")), " + function.getQuery() + ")";
			case "'min'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + flipFuncName + "(" + num + ", " + funcName + "(" + (num + offset) + ", mmin(1h, " + function.getQuery() + ")), " + function.getQuery() + ")";
			case "'max'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + flipFuncName + "(" + num + ", " + funcName + "(" + (num + offset) + ", mmax(1h, " + function.getQuery() + ")), " + function.getQuery() + ")";
            case "'sum'":
			case "'area'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + flipFuncName + "(" + num + ", " + funcName + "(" + (num + offset) + ", msum(1h, " + function.getQuery() + ")), " + function.getQuery() + ")";
			case "'l2norm'":
			case "'norm'":
				logger.warn(aggr + " has a fixed window of 1h on top function");
				return "if(" + flipFuncName + "(" + num + ", " + funcName + "(" + (num + offset) + ", msum(1h, pow(" + function.getQuery() + ", 2))), " + function.getQuery() + ")";
			default:
				break;
		}

		logger.error("unsupported aggregate for top: " + aggr);
		return function.getQuery();
	}

	public static String topConvenience(DatadogFunction function) {
		String name = function.getName();
		String dir, num, aggr;

		int ind = name.indexOf("_");
		if (ind < 0) {
			ind = name.length();
			aggr = "'mean'";
		} else {
			aggr = "'" + name.substring(ind + 1) + "'";
		}

		if (name.startsWith("top")) {
			dir = "'desc'";
			num = name.substring("top".length(), ind);
		} else {
			dir = "'asc'";
			num = name.substring("bottom".length(), ind);
		}

		ArrayList<String> arguments = new ArrayList();
		arguments.add(num);
		arguments.add(aggr);
		arguments.add(dir);

		return top(new DatadogFunction("top", arguments));
	}

}
