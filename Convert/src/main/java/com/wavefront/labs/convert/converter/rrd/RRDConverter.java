package com.wavefront.labs.convert.converter.rrd;

import com.wavefront.labs.convert.Converter;
import com.wavefront.labs.convert.converter.rrd.models.Graph;
import com.wavefront.labs.convert.converter.rrd.models.graph.GraphItem;
import com.wavefront.rest.models.Chart;
import com.wavefront.rest.models.ChartSettings;
import com.wavefront.rest.models.ChartSettings.TypeEnum;
import com.wavefront.rest.models.ChartSourceQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RRDConverter implements Converter {

	protected Properties properties;
	protected List<RRDContext> rrdContexts;

	public RRDConverter() {
		this.properties = new Properties();
		rrdContexts = new ArrayList();
	}

	@Override
	public void init(Properties properties) {
		this.properties = properties;
	}

	@Override
	public void parse(Object data) throws IOException {

		RRDContext rrdContext = new RRDContext(properties);
		rrdContext.parse(data);

		rrdContexts.add(rrdContext);
	}

	@Override
	public List convert() {

		ArrayList models = new ArrayList();

		for (RRDContext rrdContext : rrdContexts) {
			Chart chart = new Chart();

			boolean stacked = true;

			for (Graph graph : rrdContext.getGraphs()) {

				GraphItem graphItem = graph.getGraphItem();

				stacked = stacked && graphItem.isStacked();

				ChartSourceQuery chartSourceQuery = new ChartSourceQuery();
				chartSourceQuery.setName(graphItem.getLegend());

				String value = graphItem.getValue();
				if (rrdContext.hasVariable(value)) {
					value = rrdContext.getVariable(value);
				}
				chartSourceQuery.setQuery(value);

				chart.getSources().add(chartSourceQuery);
			}

			ChartSettings chartSettings = new ChartSettings();
			if (stacked) {
				chartSettings.setType(TypeEnum.STACKED_AREA);
			} else {
				chartSettings.setType(TypeEnum.LINE);
			}
			chart.setChartSettings(chartSettings);

			models.add(chart);
		}

		return models;
	}
}
