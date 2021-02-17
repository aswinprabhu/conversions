package com.wavefront.labs.convert.converter.grafana;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavefront.labs.convert.Converter;
import com.wavefront.labs.convert.ExpressionBuilder;
import com.wavefront.labs.convert.SimpleExpressionBuilder;
import com.wavefront.labs.convert.Utils;
import com.wavefront.labs.convert.converter.datadog.DatadogTimeboardConverter;
import com.wavefront.labs.convert.converter.grafana.model.GrafanaDashboard;
import com.wavefront.labs.convert.converter.grafana.model.GrafanaPanel;
import com.wavefront.rest.models.Chart;
import com.wavefront.rest.models.Dashboard;
import com.wavefront.rest.models.DashboardSection;
import com.wavefront.rest.models.DashboardSectionRow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class GrafanaConverter implements Converter {
	private static final Logger logger = LogManager.getLogger(DatadogTimeboardConverter.class);

	private Properties properties;

	private List<GrafanaDashboard> grafanaDashboards;

	private ExpressionBuilder expressionBuilder;
	private GrafanaConverterHelper grafanaConverterHelper;

	@Override
	public void init(Properties properties) {
		this.properties = properties;
		String expressionBuilderClass = properties.getProperty("convert.expressionBuilder", "");
		if (!expressionBuilderClass.equals("")) {
			try {
				expressionBuilder = (ExpressionBuilder) Class.forName(expressionBuilderClass).newInstance();
			} catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
				logger.error("Could not create instance of: " + expressionBuilderClass, e);
				expressionBuilder = new SimpleExpressionBuilder();
			}
		} else {
			expressionBuilder = new SimpleExpressionBuilder();
		}
		expressionBuilder.init(properties);
		grafanaConverterHelper = new GrafanaConverterHelper(expressionBuilder);
		grafanaDashboards = new ArrayList();
	}

	@Override
	public void parse(Object data) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		GrafanaDashboard grafanaDashboard = mapper.readValue((File) data, GrafanaDashboard.class);
		grafanaDashboards.add(grafanaDashboard);
	}

	@Override
	public List convert() {

		List models = new ArrayList();

		for (GrafanaDashboard grafanaDashboard : grafanaDashboards) {
			Dashboard dashboard = new Dashboard();
			dashboard.setUrl(Utils.sluggify(grafanaDashboard.getTitle()));
			dashboard.setName(grafanaDashboard.getTitle());
			dashboard.setDescription(grafanaDashboard.getDescription());

			List<GrafanaPanel> panels = grafanaDashboard.getPanels();
			panels.sort(Comparator.comparing(GrafanaPanel::getGridPosY).thenComparing(GrafanaPanel::getGridPosX));
			DashboardSection dashboardSection = new DashboardSection();
			dashboardSection.setName("Charts");

			DashboardSectionRow dashboardSectionRow = new DashboardSectionRow();
			int curY = 0;
			boolean rowStart = true;
			for (GrafanaPanel panel : panels) {

				if (rowStart) {
					rowStart = false;
					curY = panel.getGridPosY();
				} else if (curY != panel.getGridPosY()) {
					dashboardSection.addRowsItem(dashboardSectionRow);
					dashboardSectionRow = new DashboardSectionRow();
					curY = panel.getGridPosY();
				}

				if (panel.getType().equals("row")) {
					if (!dashboardSection.getRows().isEmpty()) {
						dashboard.addSectionsItem(dashboardSection);
						dashboardSection = new DashboardSection();
					}
					dashboardSection.setName(panel.getTitle());
					rowStart = true;
				} else {
					Chart chart = grafanaConverterHelper.buildChart(panel);
					dashboardSectionRow.addChartsItem(chart);
				}
			}

			dashboardSection.addRowsItem(dashboardSectionRow);
			dashboard.addSectionsItem(dashboardSection);

			models.add(dashboard);
		}

		return models;
	}
}
