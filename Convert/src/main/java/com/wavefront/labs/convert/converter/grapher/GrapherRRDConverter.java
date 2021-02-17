package com.wavefront.labs.convert.converter.grapher;

import com.wavefront.labs.convert.converter.grapher.models.GrapherChart;
import com.wavefront.labs.convert.converter.rrd.RRDContext;
import com.wavefront.labs.convert.converter.rrd.RRDConverter;
import com.wavefront.labs.convert.converter.rrd.models.Graph;
import com.wavefront.labs.convert.converter.rrd.models.graph.Area;
import com.wavefront.labs.convert.converter.rrd.models.graph.GraphItem;
import com.wavefront.labs.convert.converter.rrd.models.graph.GraphItemType;
import com.wavefront.labs.convert.converter.rrd.models.graph.Stack;
import com.wavefront.rest.models.Alert;
import com.wavefront.rest.models.Alert.SeverityEnum;
import com.wavefront.rest.models.Chart;
import com.wavefront.rest.models.Chart.SummarizationEnum;
import com.wavefront.rest.models.ChartSettings;
import com.wavefront.rest.models.ChartSettings.TypeEnum;
import com.wavefront.rest.models.ChartSourceQuery;

import java.util.ArrayList;
import java.util.List;

public class GrapherRRDConverter extends RRDConverter {

	private GrapherChart grapherChart;

	void setGrapherChart(GrapherChart grapherChart) {
		this.grapherChart = grapherChart;
	}

	@Override
	public List convert() {
		ArrayList models = new ArrayList();
		String notifications = properties.getProperty("grapher.alert.notifications", "test@alert.com");

		for (RRDContext rrdContext : rrdContexts) {
			Chart chart = new Chart();
			boolean chartAlertEvents = false;
			boolean stacked = true;
			int graphCount = rrdContext.getGraphs().size();
			for (Graph graph : rrdContext.getGraphs()) {

				GraphItem graphItem = graph.getGraphItem();

				stacked = stacked && ((graphItem instanceof Stack) || (graphCount == 1 && graphItem instanceof Area) || graphItem.isStacked());

				if (graph.getGraphItemType() == GraphItemType.AREA && graphItem.getColor().startsWith("COLORST")) {
					Alert alert = new Alert();
					String value = graphItem.getValue();

					alert.setName(grapherChart.getTitle() + " -- " + value);

					if (rrdContext.hasVariable(value)) {
						value = rrdContext.getVariable(value);
					}
					alert.setCondition(value);
					alert.setTarget(notifications);

					switch (graphItem.getColor()) {
						case "COLORSTCRIT":
							alert.setSeverity(SeverityEnum.SEVERE);
							break;
						case "COLORSTWARN":
							alert.setSeverity(SeverityEnum.WARN);
							break;
						case "COLORSTUNKN":
							alert.setSeverity(SeverityEnum.SMOKE);
							break;
						default:
							alert.setSeverity(SeverityEnum.INFO);
							break;
					}

					models.add(alert);

					if (!chartAlertEvents) {
						ChartSourceQuery chartSourceQuery = new ChartSourceQuery();
						chartSourceQuery.setName("Alerts");
						chartSourceQuery.setQuery("events(type=alert, name=\"" + grapherChart.getTitle() + " -- *\")");
						chartSourceQuery.setDisabled(false);
						chart.getSources().add(chartSourceQuery);
						chartAlertEvents = true;
					}

				} else {
					ChartSourceQuery chartSourceQuery = new ChartSourceQuery();
					chartSourceQuery.setName(graphItem.getLegend());

					String value = graphItem.getValue();
					if (rrdContext.hasVariable(value)) {
						value = rrdContext.getVariable(value);
					}
					chartSourceQuery.setQuery(value);

					chartSourceQuery.setDisabled(false);
					chart.getSources().add(chartSourceQuery);
				}

			}

			ChartSettings chartSettings = new ChartSettings();
			if (stacked) {
				chartSettings.setType(TypeEnum.STACKED_AREA);
			} else {
				chartSettings.setType(TypeEnum.LINE);
			}
			chartSettings.setShowHosts(false);
			chartSettings.setShowLabels(false);
			chartSettings.setShowRawValues(false);
			chartSettings.setTimeBasedColoring(false);
			chart.setChartSettings(chartSettings);

			chart.setName(grapherChart.getTitle());
			chart.setDescription(grapherChart.getDescription());
			chart.setUnits(grapherChart.getUnits());
			chart.setSummarization(SummarizationEnum.MEAN);
			chart.setBase(1);

			models.add(chart);
		}

		return models;
	}
}
