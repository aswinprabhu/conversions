package com.wavefront.labs.convert.converter.grafana;

import com.wavefront.labs.convert.ExpressionBuilder;
import com.wavefront.labs.convert.converter.grafana.model.*;
import com.wavefront.rest.models.Chart;
import com.wavefront.rest.models.Chart.SummarizationEnum;
import com.wavefront.rest.models.ChartSettings;
import com.wavefront.rest.models.ChartSettings.*;
import com.wavefront.rest.models.ChartSourceQuery;

import java.util.ArrayList;
import java.util.List;

public class GrafanaConverterHelper {

	private ExpressionBuilder expressionBuilder;

	public GrafanaConverterHelper(ExpressionBuilder expressionBuilder) {
		this.expressionBuilder = expressionBuilder;
	}

	public Chart buildChart(GrafanaPanel panel) {

		switch (panel.getType()) {
			case "graph":
				return fromGraph(panel);
			case "singlestat":
				return fromSinglestat(panel);
			case "table":
				return fromTable(panel);
			case "text":
				return fromText(panel);
			default:
				return fromDefaultPanel(panel);
		}
	}

	private Chart fromGraph(GrafanaPanel panel) {
		Chart chart = new Chart();

		chart.setName(panel.getTitle());
		chart.setDescription(panel.getDescription());
		chart.setSummarization(SummarizationEnum.MEAN);

		ChartSettings chartSettings = new ChartSettings();


		if (!panel.isLines()) {
			chartSettings.setType(TypeEnum.SCATTERPLOT);
		} else if (panel.isStack()) {
			chartSettings.setType(TypeEnum.STACKED_AREA);
			if (panel.isPercentage()) {
				chartSettings.setStackType(StackTypeEnum.EXPAND);
			}
		} else {
			chartSettings.setType(TypeEnum.LINE);
		}

		// Queries
		chart.setSources(fromTargets(panel.getTargets()));

		// Y-axes
		if (panel.getYaxes() != null && panel.getYaxes().size() > 0) {
			chart.setBase(panel.getYaxes().get(0).getLogBase());

			GrafanaPanelYAxis y0 = panel.getYaxes().get(0);

			AxisUnit y0AxisUnit = fromFormat(y0.getFormat());
			chart.setUnits(y0AxisUnit.getUnit());
			chartSettings.setY0UnitAutoscaling(y0AxisUnit.isDynamic());
			chartSettings.setY0ScaleSIBy1024(y0AxisUnit.isBinary());

			if (y0.getMin() != null && !y0.getMin().equals("")) {
				chartSettings.setYmin(Double.valueOf(y0.getMin()));
			}
			if (y0.getMax() != null && !y0.getMax().equals("")) {
				chartSettings.setYmax(Double.valueOf(y0.getMax()));
			}

			if (panel.getYaxes().size() > 1) {
				GrafanaPanelYAxis y1 = panel.getYaxes().get(1);

				AxisUnit y1AxisUnit = fromFormat(y1.getFormat());
				chartSettings.setY1Units(y1AxisUnit.getUnit());
				chartSettings.setY1UnitAutoscaling(y1AxisUnit.isDynamic());
				chartSettings.setY1ScaleSIBy1024(y1AxisUnit.isBinary());

				if (y1.getMin() != null && !y1.getMin().equals("")) {
					chartSettings.setYmin(Double.valueOf(y1.getMin()));
				}
				if (y1.getMax() != null && !y1.getMax().equals("")) {
					chartSettings.setYmax(Double.valueOf(y1.getMax()));
				}
			}
		}

		// Legend
		GrafanaPanelLegend legend = panel.getLegend();
		if (legend != null && legend.isShow()) {

			if (legend.isCurrent()) {
				chartSettings.addFixedLegendDisplayStatsItem("CURRENT");
			}
			if (legend.isAvg()) {
				chartSettings.addFixedLegendDisplayStatsItem("MEAN");
			}
			if (legend.isMin()) {
				chartSettings.addFixedLegendDisplayStatsItem("MIN");
			}
			if (legend.isMax()) {
				chartSettings.addFixedLegendDisplayStatsItem("MAX");
			}
			if (legend.isTotal()) {
				chartSettings.addFixedLegendDisplayStatsItem("SUM");
			}
			if (legend.isValues()) {
				chartSettings.addFixedLegendDisplayStatsItem("COUNT");
 			}

 			if (chartSettings.getFixedLegendDisplayStats() != null && !chartSettings.getFixedLegendDisplayStats().isEmpty()) {
			    chartSettings.setFixedLegendEnabled(true);
			    if (legend.isRightSide()) {
				    chartSettings.setFixedLegendPosition(FixedLegendPositionEnum.RIGHT);
			    } else {
				    chartSettings.setFixedLegendPosition(FixedLegendPositionEnum.BOTTOM);
			    }
		    }
 			
		}

		chart.setChartSettings(chartSettings);
		return chart;
	}

	private Chart fromSinglestat(GrafanaPanel panel) {
		Chart chart = new Chart();
		chart.setName(panel.getTitle());
		chart.setDescription(panel.getDescription());
		chart.setSummarization(SummarizationEnum.MEAN);

		ChartSettings chartSettings = new ChartSettings();

		chartSettings.setType(TypeEnum.SPARKLINE);

		// Queries
		chart.setSources(fromTargets(panel.getTargets()));

		// Format
		AxisUnit axisUnit = fromFormat(panel.getFormat());
		chart.setUnits(axisUnit.getUnit());
		chartSettings.setY0UnitAutoscaling(axisUnit.isDynamic());
		chartSettings.setY0ScaleSIBy1024(axisUnit.isBinary());

		// Sparkline 
		if (panel.getSparkline() != null) {
			GrafanaPanelSparkline sparkline = panel.getSparkline();
			if (sparkline.isShow()) {
				chartSettings.setSparklineFillColor(sparkline.getFillColor());
				chartSettings.setSparklineLineColor(sparkline.getFillColor());
				if (sparkline.isFull()) {
					chartSettings.setSparklineSize(SparklineSizeEnum.BACKGROUND);
				} else {
					chartSettings.setSparklineSize(SparklineSizeEnum.BOTTOM);
				}
			} else {
				chartSettings.setSparklineSize(SparklineSizeEnum.NONE);
			}
		} else {
			chartSettings.setSparklineSize(SparklineSizeEnum.NONE);
		}

		// Color Map Colors
		ArrayList<String> chartColors = new ArrayList();
		if (panel.getColors() instanceof ArrayList) {
			ArrayList<String> colors = (ArrayList<String>) panel.getColors();
			chartColors.addAll(colors);
		}
		chartSettings.setSparklineValueColorMapColors(chartColors);

		// Color Map Values
		ArrayList<Float> values = new ArrayList();
		if (panel.getThresholds() instanceof String) {
			String[] valueStrs = ((String) panel.getThresholds()).split(",");
			for (String value : valueStrs) {
				try {
					values.add(Float.valueOf(value));
				} catch (NumberFormatException e) {
					values.add(1f);
				}
			}
		} else {
			values.add(1f);
		}
		chartSettings.setSparklineValueColorMapValuesV2(values);

		// Color applies to
		if (panel.isColorBackground()) {
			chartSettings.setSparklineValueColorMapApplyTo(SparklineValueColorMapApplyToEnum.BACKGROUND);
		} else {
			chartSettings.setSparklineValueColorMapApplyTo(SparklineValueColorMapApplyToEnum.TEXT);
		}

		// Decimal precision
		if (panel.getDecimals() != null) {
			chartSettings.setSparklineDecimalPrecision(panel.getDecimals());
		}

		// Fontsize
		chartSettings.setSparklineDisplayFontSize(fromValueFontSize(panel.getValueFontSize()));

		// Postfix / Prefix
		chartSettings.setSparklineDisplayPostfix(panel.getPostfix());
		chartSettings.setSparklineDisplayPrefix(panel.getPrefix());

		chart.setChartSettings(chartSettings);
		return chart;
	}

	private Chart fromTable(GrafanaPanel panel) {
		Chart chart = new Chart();
		chart.setName(panel.getTitle());
		chart.setDescription(panel.getDescription());

		ChartSettings chartSettings = new ChartSettings();
		chartSettings.setType(TypeEnum.TABLE);

		// Queries
		chart.setSources(fromTargets(panel.getTargets()));

		chart.setChartSettings(chartSettings);
		return chart;
	}

	private Chart fromText(GrafanaPanel panel) {
		Chart chart = fromDefaultPanel(panel);

		chart.getChartSettings().setPlainMarkdownContent(panel.getContent());
		
		return chart;
	}

	private Chart fromDefaultPanel(GrafanaPanel panel) {
		Chart chart = new Chart();
		chart.setName(panel.getTitle());
		chart.setDescription(panel.getDescription());

		ChartSettings chartSettings = new ChartSettings();
		chartSettings.setType(TypeEnum.MARKDOWN_WIDGET);

		chartSettings.setPlainMarkdownContent(panel.getType());

		chart.setChartSettings(chartSettings);
		return chart;
	}

	private List<ChartSourceQuery> fromTargets(List<GrafanaPanelTarget> targets) {
		List<ChartSourceQuery> chartSourceQueries = new ArrayList();

		if (targets != null) {
			for (GrafanaPanelTarget target : targets) {
				ChartSourceQuery chartSourceQuery = new ChartSourceQuery();
				chartSourceQuery.setDisabled(target.isHide());
				chartSourceQuery.setName(target.getRefId());

				String query = target.getTargetFull() != null && !target.getTargetFull().equals("") ? target.getTargetFull() : target.getTarget();
				if (query != null && !query.equals("")) {
					chartSourceQuery.setQuery(expressionBuilder.buildExpression(query));
				}

				chartSourceQueries.add(chartSourceQuery);
			}
		}

		return chartSourceQueries;
	}

	private AxisUnit fromFormat(String format) {
		if (format == null || format.equals("") || format.equals("none") || format.equals("short")) {
			return new AxisUnit("", false, false);
		}

		format = format.toLowerCase();

		if (format.equals("percent")) {
			return new AxisUnit("%", false, false);
		}

		if (format.startsWith("dtduration")) {
			return new AxisUnit(format.substring("dtduration".length()), true, false);
		}

		if (format.startsWith("dec")) {
			format = format.substring("dec".length());
		}

		if (format.endsWith("bytes")) {
			return new AxisUnit(format.substring(0, format.length() - 4).toUpperCase(), true, false);

		}

		if (format.endsWith("bits")) {
			format = format.substring(0, format.length() - 3).toUpperCase();
			if (format.length() > 1) {
				format = format.charAt(0) + "i" + format.charAt(1);
			}
			return new AxisUnit(format, true, true);
		}

		return new AxisUnit(format, true, false);

	}

	private String fromValueFontSize(String valueFontSize) {
		if (valueFontSize == null) {
			return "100";
		}

		int value = Integer.parseInt(valueFontSize.replace("%", ""));

		if (value >= 200) {
			return "200";
		} else if (value >= 175) {
			return "175";
		} else if (value >= 150) {
			return "150";
		} else if (value >= 125) {
			return "125";
		} else if (value >= 100) {
			return "100";
		} else {
			return "75";
		}
	}

	private class AxisUnit {
		private String unit;
		private boolean dynamic;
		private boolean binary;

		public AxisUnit(String unit, boolean dynamic, boolean binary) {
			this.unit = unit;
			this.dynamic = dynamic;
			this.binary = binary;
		}

		public String getUnit() {
			return unit;
		}

		public boolean isDynamic() {
			return dynamic;
		}

		public boolean isBinary() {
			return binary;
		}
	}
}
