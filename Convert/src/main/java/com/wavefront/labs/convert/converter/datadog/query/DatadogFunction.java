package com.wavefront.labs.convert.converter.datadog.query;

import java.util.ArrayList;
import java.util.List;

public class DatadogFunction {

	private String name;
	private List<String> arguments;
	private String query = null;

	public DatadogFunction(String name) {
		this.name = name;
		arguments = new ArrayList();
	}

	public DatadogFunction(String name, List<String> arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
