package com.wavefront.labs.convert.converter.rrd.models;

import com.wavefront.labs.convert.converter.rrd.RRDContext;
import com.wavefront.labs.convert.converter.rrd.rpn.NotSupported;
import com.wavefront.labs.convert.converter.rrd.rpn.Variables;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.function.Function;

public class VDef extends Definition {

	private static HashMap<String, Function<Deque<String>, String>> functionMap = new HashMap();
	static {
		functionMap.put("MAXIMUM", NotSupported::warning);
		functionMap.put("MINIMUM", NotSupported::warning);
		functionMap.put("AVERAGE", NotSupported::warning);
		functionMap.put("STDEV", NotSupported::warning);
		functionMap.put("LAST", Variables::last);
		functionMap.put("FIRST", Variables::first);
		functionMap.put("TOTAL", Variables::total);
		functionMap.put("PERCENT", NotSupported::warning);
		functionMap.put("PERCENTNAN", NotSupported::warning);
		functionMap.put("LSLSLOPE", NotSupported::warning);
		functionMap.put("LSLINT", NotSupported::warning);
		functionMap.put("LSLCORREL", NotSupported::warning);
	}

	public VDef (String line) {
		super(line);
	}

	@Override
	public String calculate(RRDContext context) {

		Deque<String> queue = new ArrayDeque();

		String[] parts = expression.split(",");
		for (String part : parts) {
			if (functionMap.containsKey(part)) {
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
