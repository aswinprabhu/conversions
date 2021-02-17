package com.wavefront.labs.convert.converter.datadog.models;

public class DatadogGraph {

	private DatadogGraphDefinition definition;
	private String title;

	public DatadogGraphDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(DatadogGraphDefinition definition) {
		this.definition = definition;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
