package com.wavefront.labs.convert.converter.rrd.models.graph;

import com.wavefront.labs.convert.converter.rrd.RRDContext;

public class Area extends GraphItem {

	public Area(String line, RRDContext context) {
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
		if (tmp.equals("STACK")) {
			stacked = true;
			return;
		}

		if (tmp.equals("skipscale")) {
			return;
		}

		legend = stripQuotes(tmp);

		ind++;
		if (ind >= parts.length) {
			return;
		}
		tmp = parts[ind];
		if (tmp.equals("STACK")) {
			stacked = true;
		}
	}
}
