package com.wavefront.labs.convert.converter.rrd.models.graph;

import com.wavefront.labs.convert.converter.rrd.RRDContext;

public class Stack extends Area {

	public Stack(String line, RRDContext context) {
		super(line, context);
	}

	@Override
	public void process() {
		stacked = true;
		super.process();
	}
}
