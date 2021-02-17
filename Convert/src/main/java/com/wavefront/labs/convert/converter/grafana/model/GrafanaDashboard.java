package com.wavefront.labs.convert.converter.grafana.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GrafanaDashboard {

	@JsonProperty("__inputs")
	private ArrayList<HashMap<String, String>> inputs;

	private Object annotations;
	private String description;
	private List<GrafanaPanel> panels;
	private String title;
	private String uid;

	public ArrayList<HashMap<String, String>> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<HashMap<String, String>> inputs) {
		this.inputs = inputs;
	}

	public Object getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Object annotations) {
		this.annotations = annotations;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<GrafanaPanel> getPanels() {
		return panels;
	}

	public void setPanels(List<GrafanaPanel> panels) {
		this.panels = panels;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
