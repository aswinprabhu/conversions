package com.wavefront.labs.convert.converter.rrd.models.graph;

import com.wavefront.labs.convert.converter.rrd.RRDContext;

public class Line extends GraphItem {

	public Line(String line, RRDContext context) {
		super(line, context);
	}

	@Override
	public void process() {
		String[] parts = line.split(":");
		int ind = 1;

		if (checkNumeric(parts[ind])) {
			ind++;
		}

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

		if (tmp.equals("skipscale") || tmp.startsWith("dashes") || tmp.startsWith("dash-offset=")) {
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

	private boolean checkNumeric(String val) {
		try {
			Double.parseDouble(val);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
