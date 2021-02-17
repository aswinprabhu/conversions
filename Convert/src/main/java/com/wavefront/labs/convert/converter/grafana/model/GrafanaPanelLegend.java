package com.wavefront.labs.convert.converter.grafana.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GrafanaPanelLegend {

	private boolean alignAsTable;
	private boolean avg;
	private boolean current;
	private boolean max;
	private boolean min;
	private boolean rightSide;
	private boolean show;
	private boolean total;
	private boolean values;
	private boolean sortDesc;

	public boolean isAlignAsTable() {
		return alignAsTable;
	}

	public void setAlignAsTable(boolean alignAsTable) {
		this.alignAsTable = alignAsTable;
	}

	public boolean isAvg() {
		return avg;
	}

	public void setAvg(boolean avg) {
		this.avg = avg;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public boolean isMax() {
		return max;
	}

	public void setMax(boolean max) {
		this.max = max;
	}

	public boolean isMin() {
		return min;
	}

	public void setMin(boolean min) {
		this.min = min;
	}

	public boolean isRightSide() {
		return rightSide;
	}

	public void setRightSide(boolean rightSide) {
		this.rightSide = rightSide;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isTotal() {
		return total;
	}

	public void setTotal(boolean total) {
		this.total = total;
	}

	public boolean isValues() {
		return values;
	}

	public void setValues(boolean values) {
		this.values = values;
	}

	public boolean isSortDesc() {
		return sortDesc;
	}

	public void setSortDesc(boolean sortDesc) {
		this.sortDesc = sortDesc;
	}
}
