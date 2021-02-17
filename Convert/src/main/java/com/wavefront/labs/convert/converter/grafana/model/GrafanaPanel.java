package com.wavefront.labs.convert.converter.grafana.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GrafanaPanel {

	private HashMap alert;
	private boolean colorBackground;
	private Object colors;
	private String content;
	private Integer decimals;
	private String description;
	private String format;
	private GridPos gridPos;
	private GrafanaPanelLegend legend;
	private boolean lines;
	private boolean percentage;
	private String postfix;
	private String prefix;
	private GrafanaPanelSparkline sparkline;
	private boolean stack;
	private ArrayList<GrafanaPanelTarget> targets;
	private Object thresholds;
	private String title;
	private String type;
	private ArrayList<GrafanaPanelYAxis> yaxes;
	private String valueFontSize;

	public HashMap getAlert() {
		return alert;
	}

	public void setAlert(HashMap alert) {
		this.alert = alert;
	}

	public boolean isColorBackground() {
		return colorBackground;
	}

	public void setColorBackground(boolean colorBackground) {
		this.colorBackground = colorBackground;
	}

	public Object getColors() {
		return colors;
	}

	public void setColors(Object colors) {
		this.colors = colors;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public GridPos getGridPos() {
		return gridPos;
	}

	public void setGridPos(GridPos gridPos) {
		this.gridPos = gridPos;
	}

	public int getGridPosX() {
		return this.getGridPos().getX();
	}

	public int getGridPosY() {
		return this.getGridPos().getY();
	}

	public GrafanaPanelLegend getLegend() {
		return legend;
	}

	public void setLegend(GrafanaPanelLegend legend) {
		this.legend = legend;
	}

	public boolean isLines() {
		return lines;
	}

	public void setLines(boolean lines) {
		this.lines = lines;
	}

	public boolean isPercentage() {
		return percentage;
	}

	public void setPercentage(boolean percentage) {
		this.percentage = percentage;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public GrafanaPanelSparkline getSparkline() {
		return sparkline;
	}

	public void setSparkline(GrafanaPanelSparkline sparkline) {
		this.sparkline = sparkline;
	}

	public boolean isStack() {
		return stack;
	}

	public void setStack(boolean stack) {
		this.stack = stack;
	}

	public ArrayList<GrafanaPanelTarget> getTargets() {
		return targets;
	}

	public void setTargets(ArrayList<GrafanaPanelTarget> targets) {
		this.targets = targets;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<GrafanaPanelYAxis> getYaxes() {
		return yaxes;
	}

	public void setYaxes(ArrayList<GrafanaPanelYAxis> yaxes) {
		this.yaxes = yaxes;
	}

	public Object getThresholds() {
		return thresholds;
	}

	public void setThresholds(Object thresholds) {
		this.thresholds = thresholds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public class GridPos {
		private int h;
		private int w;
		private int x;
		private int y;

		public int getH() {
			return h;
		}

		public void setH(int h) {
			this.h = h;
		}

		public int getW() {
			return w;
		}

		public void setW(int w) {
			this.w = w;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}
	}

	public String getValueFontSize() {
		return valueFontSize;
	}

	public void setValueFontSize(String valueFontSize) {
		this.valueFontSize = valueFontSize;
	}
}
