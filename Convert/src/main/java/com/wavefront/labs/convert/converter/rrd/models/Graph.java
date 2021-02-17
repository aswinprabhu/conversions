package com.wavefront.labs.convert.converter.rrd.models;

import com.wavefront.labs.convert.converter.rrd.RRDContext;
import com.wavefront.labs.convert.converter.rrd.models.graph.GraphItem;
import com.wavefront.labs.convert.converter.rrd.models.graph.GraphItemType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class Graph {
	private static final Logger logger = LogManager.getLogger(Graph.class);

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
			logger.error("Could not process graph item: " + line, e);
		}

	}

	public GraphItemType getGraphItemType() {
		return graphItemType;
	}

	public GraphItem getGraphItem() {
		return graphItem;
	}
}
