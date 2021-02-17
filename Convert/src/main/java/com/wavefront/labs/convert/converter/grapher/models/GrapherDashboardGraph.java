package com.wavefront.labs.convert.converter.grapher.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wavefront.labs.convert.converter.grapher.GrapherChartConverter;

public class GrapherDashboardGraph {

	@JsonProperty(":separator")
	private String separator;

	@JsonProperty(":graph")
	private String graph;

	@JsonProperty(":path")
	private String path;

	@JsonProperty(":size")
	private String size;

	private String filter = "";
	private GrapherChartConverter converter;

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public GrapherChartConverter getConverter() {
		return converter;
	}

	public void setConverter(GrapherChartConverter converter) {
		this.converter = converter;
	}
}