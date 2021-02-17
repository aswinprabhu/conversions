package com.wavefront.labs.convert.converter.grapher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GrapherChart {

	private String category, description, title, units;

	@JsonProperty("splunk_url")
	private String splunkUrl;

	@JsonProperty("rrd_def")
	private String rrdDef;

	@JsonProperty("rrd_def_host")
	private String rrdDefHost;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getSplunkUrl() {
		return splunkUrl;
	}

	public void setSplunkUrl(String splunkUrl) {
		this.splunkUrl = splunkUrl;
	}

	public String getRrdDef() {
		return rrdDef;
	}

	public void setRrdDef(String rrdDef) {
		this.rrdDef = rrdDef;
	}

	public String getRrdDefHost() {
		return rrdDefHost;
	}

	public void setRrdDefHost(String rrdDefHost) {
		this.rrdDefHost = rrdDefHost;
	}
}
