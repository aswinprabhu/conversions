package com.wavefront.labs.convert.converter.datadog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"style", "conditional_formats", "metadata", "extra_col", "limit", "change_type", "order_dir", "order", "compare_to", "process_query", "increase_good", "order_by", "display_type", "show_present", "alias"})
public class DatadogGraphRequest {

	@JsonProperty("q")
	private String query;
	private String aggregator;
	private String type;

	//private DatadogGraphRequest x;
	//private DatadogGraphRequest y;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getAggregator() {
		return aggregator;
	}

	public void setAggregator(String aggregator) {
		this.aggregator = aggregator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//public DatadogGraphRequest getX() { return x; }

	//public void setX(DatadogGraphRequest x) { this.x = x; }

	//public DatadogGraphRequest getY() { return y; }

	//public void setY(DatadogGraphRequest y) { this.y = y; }
}
