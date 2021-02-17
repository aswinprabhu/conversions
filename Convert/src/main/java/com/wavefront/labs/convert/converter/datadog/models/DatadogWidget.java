package com.wavefront.labs.convert.converter.datadog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"layout"})
public class DatadogWidget {

	private DatadogGraphDefinition definition;
	private String id;

	public DatadogGraphDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(DatadogGraphDefinition definition) {
		this.definition = definition;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
