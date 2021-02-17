package com.wavefront.labs.convert.converter.rrd.models.graph;

import com.wavefront.labs.convert.converter.rrd.RRDContext;

public class Shift extends GraphItem {

	private String offset;

	public Shift(String line, RRDContext context) {
		super(line, context);
	}

	@Override
	public void process() {
		String[] parts = line.split(":");

		value = stripQuotes(parts[0]);
		offset = parts[1];
		if (context.hasVariable(value)) {
			String ts = "lag(" + offset + "s, " + context.getVariable(value) + ")";
			context.getVariables().put(value, ts);
		}

	}

	public String getOffset() {
		return offset;
	}
}
