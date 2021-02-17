package com.wavefront.labs.convert.converter.datadog.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties({"notify_list", "author_name", "is_read_only", "url", "created_at", "modified_at", "modified", "author_handle", "layout_type", "template_variable_presets"})
public class DatadogTimeboard {

	@JsonProperty("template_variables")
	private List<DatadogTemplateVariable> templateVariables;
	private String description;
	private String id;
	private String title;
	private List<DatadogWidget> widgets;

	public List<DatadogTemplateVariable> getTemplateVariables() {
		return templateVariables;
	}

	public void setTemplateVariables(List<DatadogTemplateVariable> templateVariables) {
		this.templateVariables = templateVariables;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<DatadogWidget> getWidgets() { return widgets; }

	public void setWidgets(List<DatadogWidget> widgets) {
		this.widgets = widgets;
	}
}
