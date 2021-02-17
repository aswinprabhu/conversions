package com.wavefront.labs.convert.converter.rrd.models;

import com.wavefront.labs.convert.converter.rrd.RRDContext;
import com.wavefront.labs.convert.converter.rrd.rpn.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.function.Function;

public class CDef extends Definition {

	private static HashMap<String, Function<Deque<String>, String>> functionMap = new HashMap();

	static {
		functionMap.put("LT", BooleanOperators::lessThan);
		functionMap.put("LE", BooleanOperators::lessEqual);
		functionMap.put("GT", BooleanOperators::greaterThan);
		functionMap.put("GE", BooleanOperators::greaterEqual);
		functionMap.put("EQ", BooleanOperators::equal);
		functionMap.put("NE", BooleanOperators::notEqual);
		functionMap.put("UN", BooleanOperators::isUnknown);
		functionMap.put("ISINF", BooleanOperators::isUnknown);
		functionMap.put("IF", BooleanOperators::_if);

		functionMap.put("MIN", ComparingValues::min);
		functionMap.put("MAX", ComparingValues::max);
		functionMap.put("MINNAN", ComparingValues::min);
		functionMap.put("MAXNAN", ComparingValues::max);
		functionMap.put("LIMIT", ComparingValues::between);

		functionMap.put("+", Arithmetics::add);
		functionMap.put("-", Arithmetics::substract);
		functionMap.put("*", Arithmetics::multiply);
		functionMap.put("/", Arithmetics::divide);
		functionMap.put("%", NotSupported::warning);
		functionMap.put("ADDNAN", Arithmetics::add);
		functionMap.put("SIN", Arithmetics::sin);
		functionMap.put("COS", Arithmetics::cos);
		functionMap.put("LOG", Arithmetics::log);
		functionMap.put("EXP", Arithmetics::exp);
		functionMap.put("SQRT", Arithmetics::sqrt);
		functionMap.put("ATAN", Arithmetics::atan);
		functionMap.put("ATAN2", Arithmetics::atan2);
		functionMap.put("FLOOR", Arithmetics::floor);
		functionMap.put("CEIL", Arithmetics::ceil);
		functionMap.put("DEG2RAD", Arithmetics::deg2rad);
		functionMap.put("RAD2DEG", Arithmetics::rad2deg);
		functionMap.put("ABS", Arithmetics::abs);


		functionMap.put("SORT", SetOperations::sort);
		functionMap.put("REV", SetOperations::rev);
		functionMap.put("AVG", SetOperations::avg);
		functionMap.put("MEDIAN", SetOperations::median);
		functionMap.put("TREND", SetOperations::trend);
		functionMap.put("TRENDNAN", SetOperations::trend);
		functionMap.put("PREDICT", SetOperations::predict);
		functionMap.put("PREDICTSIGMA", NotSupported::warning); //TODO: This might be possible, need to investigate
		functionMap.put("PREDICTPERC", NotSupported::warning);

		functionMap.put("UNKN", SpecialValues::zero);
		functionMap.put("INF", SpecialValues::one);
		functionMap.put("Inf", SpecialValues::one);
		functionMap.put("NEGINF", SpecialValues::negone);
		functionMap.put("COUNT", NotSupported::warning);

		functionMap.put("NOW", NotSupported::warning);
		functionMap.put("STEPWIDTH", NotSupported::warning);
		functionMap.put("NEWDAY", NotSupported::warning);
		functionMap.put("NEWWEEK", NotSupported::warning);
		functionMap.put("NEWMONTH", NotSupported::warning);
		functionMap.put("NEWYEAR", NotSupported::warning);
		functionMap.put("TIME", Time::time);
		functionMap.put("LTIME", Time::ltime);

		functionMap.put("DUP", StackProcessing::dup);
		functionMap.put("POP", StackProcessing::pop);
		functionMap.put("EXC", StackProcessing::exc);
		functionMap.put("DEPTH", StackProcessing::depth);
		functionMap.put("COPY", StackProcessing::copy);
		functionMap.put("INDEX", StackProcessing::index);
		functionMap.put("ROLL", StackProcessing::roll);

	}


	public CDef(String line) {
		super(line);
	}

	@Override
	public String calculate(RRDContext context) {

		Deque<String> queue = new ArrayDeque();

		String[] parts = expression.split(",");
		for (String part : parts) {

			if (part.equals("PREV") || part.startsWith("PREV(")) {
				String step = context.getProperties().getProperty("rrd.rpn.prev.step", "1m");

				if (part.startsWith("PREV(")) {
					String variableName = part.substring(5, part.length() - 2);
					String ts;
					ts = "lag(" + step + ", " + context.getVariable(variableName) + ")";
					queue.push(ts);

				} else {
					String ts;
					ts = "lag(" + step + "s, " + queue.pop() + ")";
					queue.push(ts);
				}

			} else if (functionMap.containsKey(part)) {
				String warning = functionMap.get(part).apply(queue);
				if (warning != null) {
					warnings.add(part + ": " + warning);
				}

			} else if (context.hasVariable(part)) {
				queue.push(context.getVariable(part));

			} else {
				queue.push(part);
			}

		}

		return queue.pop();
	}
}
