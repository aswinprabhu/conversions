package com.wavefront.labs.convert.converter.grafana.model;

public class GrafanaPanelYAxis {

	private String format;
	private String label;
	private int logBase;
	private String max;
	private String min;
	private boolean show;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getLogBase() {
		return logBase;
	}

	public void setLogBase(int logBase) {
		this.logBase = logBase;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}
}
