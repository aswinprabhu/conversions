package com.wavefront.labs.convert.input.rrd.models;

import com.wavefront.labs.convert.input.rrd.RRDContext;
import com.wavefront.labs.convert.input.rrd.models.graph.GraphItem;
import com.wavefront.labs.convert.input.rrd.models.graph.GraphItemType;

import java.lang.reflect.InvocationTargetException;

public class Graph {

	private GraphItemType graphItemType;
	private GraphItem graphItem;

	public Graph(String line, RRDContext context) {

		graphItemType = null;

		for (GraphItemType type : GraphItemType.values()) {
			if (line.startsWith(type.name())) {
				graphItemType = type;
				break;
			}
		}

		if (graphItemType == null) {
			graphItemType = GraphItemType.UNKNOWN;
		}

		try {
			graphItem = (GraphItem) graphItemType.getType().getConstructor(String.class, RRDContext.class).newInstance(line, context);
			graphItem.process();
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	public GraphItemType getGraphItemType() {
		return graphItemType;
	}

	public GraphItem getGraphItem() {
		return graphItem;
	}
}
