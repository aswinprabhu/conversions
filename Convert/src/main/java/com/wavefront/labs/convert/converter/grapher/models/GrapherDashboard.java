package com.wavefront.labs.convert.converter.grapher.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wavefront.labs.convert.converter.grapher.GrapherDashboardConverter;

@JsonIgnoreProperties({"created_at", "updated_at"})
public class GrapherDashboard {

	private int id;

	@JsonProperty("user_id")
	private String userId;

	private String name;
	private String data;

	private GrapherDashboardConverter converter;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public GrapherDashboardConverter getConverter() {
		return converter;
	}

	public void setConverter(GrapherDashboardConverter converter) {
		this.converter = converter;
	}
}
