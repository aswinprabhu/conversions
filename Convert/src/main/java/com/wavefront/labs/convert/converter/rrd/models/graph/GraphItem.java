package com.wavefront.labs.convert.converter.rrd.models.graph;

import com.wavefront.labs.convert.converter.rrd.RRDContext;

public abstract class GraphItem {

	String line;
	RRDContext context;

	String value = "";
	String legend = "Query";
	String color = "";
	boolean stacked = false;

	GraphItem(String line, RRDContext context) {
		this.line = line;
		this.context = context;
	}

	public abstract void process();

	String stripQuotes(String value) {
		if (value.startsWith("'")) {
			value = value.substring(1);
		}

		if (value.endsWith(("'"))) {
			value = value.substring(0, value.length() - 1);
		}

		return value;
	}

	public String getLine() {
		return line;
	}

	public RRDContext getContext() {
		return context;
	}

	public String getValue() {
		return value;
	}

	public String getLegend() {
		return legend;
	}

	public String getColor() {
		return color;
	}

	public boolean isStacked() {
		return stacked;
	}
}
