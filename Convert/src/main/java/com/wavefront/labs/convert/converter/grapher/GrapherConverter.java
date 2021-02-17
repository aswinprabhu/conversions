package com.wavefront.labs.convert.converter.grapher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavefront.labs.convert.converter.grapher.models.GrapherDashboard;
import com.wavefront.rest.models.Dashboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GrapherConverter extends AbstractGrapherConverter {

	private static final Logger logger = LogManager.getLogger(GrapherConverter.class);

	private List<List<GrapherDashboard>> grapherDashboardsList;

	@Override
	public void init(Properties properties) {
		super.init(properties);
		grapherDashboardsList = new ArrayList();
	}

	@Override
	public void parse(Object data) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		List<GrapherDashboard> grapherDashboards = new ArrayList();

		if (data instanceof String) {
			grapherDashboards = mapper.readValue((String) data, new TypeReference<List<GrapherDashboard>>() {
			});
		} else if (data instanceof File) {
			grapherDashboards = mapper.readValue((File) data, new TypeReference<List<GrapherDashboard>>() {
			});
		} else {
			logger.error("Invalid type to parse: " + data.getClass().getName());
			return;
		}

		parseModel(grapherDashboards);
		grapherDashboardsList.add(grapherDashboards);
	}

	private void parseModel(List<GrapherDashboard> grapherDashboards) throws IOException {
		for (GrapherDashboard grapherDashboard : grapherDashboards) {
			GrapherDashboardConverter grapherDashboardConverter = new GrapherDashboardConverter();
			grapherDashboardConverter.init(properties);
			grapherDashboardConverter.parse(grapherDashboard.getData());
			grapherDashboard.setConverter(grapherDashboardConverter);
		}
	}

	@Override
	public List convert() {

		List models = new ArrayList();

		for (List<GrapherDashboard> grapherDashboards : grapherDashboardsList) {

			for (GrapherDashboard grapherDashboard : grapherDashboards) {
				if (grapherDashboard.getConverter() != null) {
					List dashboardModels = grapherDashboard.getConverter().convert();

					int count = 0;
					String name = grapherDashboard.getName();
					String url = name.toLowerCase().replaceAll("[^a-zA-Z0-9_\\-]", "-");
					for (Object model : dashboardModels) {
						if (model instanceof Dashboard) {
							Dashboard dashboard = (Dashboard) model;
							if (count == 0) {
								dashboard.setUrl(url);
								dashboard.setName(name);
							} else {
								dashboard.setUrl(url + "-" + count);
								dashboard.setName(name + " - " + count);
							}
							count++;
						}
					}

					models.addAll(dashboardModels);
				}
			}
		}

		return models;
	}
}
