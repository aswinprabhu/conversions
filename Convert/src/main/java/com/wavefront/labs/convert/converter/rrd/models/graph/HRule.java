package com.wavefront.labs.convert.converter.rrd.models.graph;

import com.wavefront.labs.convert.converter.rrd.RRDContext;

public class HRule extends GraphItem {

	public HRule(String line, RRDContext context) {
		super(line, context);
	}

	@Override
	public void process() {
		String[] parts = line.split(":");
		int ind = 1;

		String[] valueParts = parts[ind].split("#");
		value = stripQuotes(valueParts[0]);
		if (valueParts.length == 2) {
			color = valueParts[1];
		}

		ind++;
		if (ind >= parts.length) {
			return;
		}
		String tmp = parts[ind];
		if (tmp.startsWith("dashes") || tmp.startsWith("dash-offset")) {
			return;
		}

		legend = stripQuotes(tmp);
	}
}
