package com.wavefront.labs.convert.converter.datadog.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatadogTemplateVariable {

	@JsonProperty("default")
	private String _default;
	private String prefix;
	private String name;

	public String get_default() {
		return _default;
	}

	public void set_default(String _default) {
		this._default = _default;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
