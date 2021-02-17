package com.wavefront.labs.convert.converter.grapher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wavefront.labs.convert.converter.grapher.models.GrapherChartDefinition;
import com.wavefront.labs.convert.converter.grapher.models.GrapherDashboardGraph;
import com.wavefront.rest.models.*;
import com.wavefront.rest.models.DashboardParameterValue.ParameterTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GrapherDashboardConverter extends AbstractGrapherConverter {

	private static final Logger logger = LogManager.getLogger(GrapherDashboardConverter.class);

	private static List<GrapherChartDefinition> grapherChartDefinitions;

	private List<GrapherDashboardGraph> grapherDashboardGraphs;

	private static List<GrapherChartDefinition> getAllGraphDefinitions(Properties properties) throws IOException {
		if (grapherChartDefinitions == null) {
			String chartDefinitionsFile = properties.getProperty("grapher.definitions.charts");
			ObjectMapper mapper = new ObjectMapper();
			grapherChartDefinitions = mapper.readValue(new File(chartDefinitionsFile), new TypeReference<List<GrapherChartDefinition>>() {
			});
		}
		return grapherChartDefinitions;
	}

	@Override
	public void parse(Object data) throws IOException {

		if (!(data instanceof String)) {
			logger.error("Invalid type to parse: " + data.getClass().getName());
			return;
		}

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		HashMap<String, List<GrapherDashboardGraph>> map = mapper.readValue((String) data, new TypeReference<HashMap<String, List<GrapherDashboardGraph>>>() {
		});
		grapherDashboardGraphs = filterGraphs(map.get(":graphs"));
		parseModel();
	}

	private void parseModel() throws IOException {

		for (GrapherDashboardGraph grapherDashboardGraph : grapherDashboardGraphs) {
			if (grapherDashboardGraph.getSeparator() == null) {

				if (grapherDashboardGraph.getFilter().equals("")) {
					properties.setProperty("grapher.filter@Current", "");
				} else {
					properties.setProperty("grapher.filter@Current", grapherDashboardGraph.getFilter());
				}

				String definition = getGraphDefinition(grapherDashboardGraph);
				if (definition != null) {
					GrapherChartConverter grapherChartConverter = new GrapherChartConverter();
					grapherChartConverter.init(properties);
					grapherChartConverter.parse(definition);
					grapherDashboardGraph.setConverter(grapherChartConverter);
				}

				properties.setProperty("grapher.filter@Current", "");
			}
		}
	}

	private List<GrapherDashboardGraph> filterGraphs(List<GrapherDashboardGraph> origGraphs) {
		LinkedHashMap<String, GrapherDashboardGraph> graphsMap = new LinkedHashMap();
		int sepCount = 0;
		for (GrapherDashboardGraph graph : origGraphs) {

			if (graph.getSeparator() != null) {
				graphsMap.put(sepCount + "-SEPERATOR", graph);
				sepCount++;

			} else {
				if (graphsMap.containsKey(graph.getGraph())) {
					graphsMap.get(graph.getGraph()).setFilter(properties.getProperty("grapher.filter.colo", "*"));
				} else {
					graphsMap.put(graph.getGraph(), graph);
				}
			}
		}

		List<GrapherDashboardGraph> graphs = new ArrayList();
		graphs.addAll(graphsMap.values());

		return graphs;
	}

	@Override
	public List convert() {
		List models = new ArrayList();

		int chartsPerRow = Integer.parseInt(properties.getProperty("grapher.dashboard.row.charts", "3"));

		Dashboard dashboard = new Dashboard();
		DashboardSection dashboardSection = null;
		DashboardSectionRow dashboardSectionRow = null;
		int chartCount = 0;
		boolean addFilter = false;

		for (GrapherDashboardGraph grapherDashboardGraph : grapherDashboardGraphs) {

			if (grapherDashboardGraph.getSeparator() != null) {

				if (dashboardSection != null) {
					dashboardSection.getRows().add(dashboardSectionRow);
					dashboard.getSections().add(dashboardSection);
				}

				dashboardSection = new DashboardSection();
				dashboardSection.setName(grapherDashboardGraph.getSeparator());
				dashboardSectionRow = new DashboardSectionRow();

			} else if (grapherDashboardGraph.getConverter() != null) {

				if (dashboardSection == null) {
					dashboardSection = new DashboardSection();
					dashboardSection.setName("Section");
					dashboardSectionRow = new DashboardSectionRow();
				}

				addFilter = addFilter || !grapherDashboardGraph.getFilter().equals("");

				List innerModels = grapherDashboardGraph.getConverter().convert();
				for (Object innerModel : innerModels) {

					if (innerModel instanceof Chart) {
						chartCount++;
						dashboardSectionRow.getCharts().add((Chart) innerModel);

						if (chartCount % chartsPerRow == 0) {
							dashboardSection.getRows().add(dashboardSectionRow);
							dashboardSectionRow = new DashboardSectionRow();
						}

					} else {
						models.add(innerModel);
					}
				}

			}

		}

		if (dashboardSection != null) {
			dashboard.addSectionsItem(dashboardSection);
		}

		dashboard.setDisplayDescription(false);
		dashboard.setDisplaySectionTableOfContents(false);
		dashboard.setDisplayQueryParameters(false);

		if (addFilter) {
			Map<String, DashboardParameterValue> dashboardParameters = new HashMap();
			dashboardParameters.put("host", createHostVariable());
			dashboardParameters.put("colo", createColoVariable());
			dashboard.setParameterDetails(dashboardParameters);
			dashboard.setDisplayQueryParameters(true);
		}

		models.add(dashboard);
		return models;
	}

	private DashboardParameterValue createHostVariable() {
		DashboardParameterValue host = new DashboardParameterValue();
		host.setParameterType(ParameterTypeEnum.LIST);
		host.setLabel("host");
		host.setDefaultValue("cluster");

		HashMap<String, String> valuesMap = new HashMap();
		valuesMap.put("cluster", "api_lazlo-cluster");
		valuesMap.put("app19", "api-lazlo-app19");
		valuesMap.put("app18", "api-lazlo-app18");
		valuesMap.put("app13", "api-lazlo-app13");
		valuesMap.put("app35", "api-lazlo-app35");
		valuesMap.put("app12", "api-lazlo-app12");
		valuesMap.put("app34", "api-lazlo-app34");
		valuesMap.put("app11", "api-lazlo-app11");
		valuesMap.put("app33", "api-lazlo-app33");
		valuesMap.put("app10", "api-lazlo-app10");
		valuesMap.put("app32", "api-lazlo-app32");
		valuesMap.put("app17", "api-lazlo-app17");
		valuesMap.put("app39", "api-lazlo-app39");
		valuesMap.put("app16", "api-lazlo-app16");
		valuesMap.put("app38", "api-lazlo-app38");
		valuesMap.put("app15", "api-lazlo-app15");
		valuesMap.put("app37", "api-lazlo-app37");
		valuesMap.put("app14", "api-lazlo-app14");
		valuesMap.put("app36", "api-lazlo-app36");
		valuesMap.put("app20", "api-lazlo-app20");
		valuesMap.put("app42", "api-lazlo-app42");
		valuesMap.put("app9", "api-lazlo-app9");
		valuesMap.put("app41", "api-lazlo-app41");
		valuesMap.put("app40", "api-lazlo-app40");
		valuesMap.put("app6", "api-lazlo-app6");
		valuesMap.put("app5", "api-lazlo-app5");
		valuesMap.put("app8", "api-lazlo-app8");
		valuesMap.put("app7", "api-lazlo-app7");
		valuesMap.put("app2", "api-lazlo-app2");
		valuesMap.put("app1", "api-lazlo-app1");
		valuesMap.put("app4", "api-lazlo-app4");
		valuesMap.put("app3", "api-lazlo-app3");
		valuesMap.put("app29", "api-lazlo-app29");
		valuesMap.put("app24", "api-lazlo-app24");
		valuesMap.put("app23", "api-lazlo-app23");
		valuesMap.put("app45", "api-lazlo-app45");
		valuesMap.put("app22", "api-lazlo-app22");
		valuesMap.put("app44", "api-lazlo-app44");
		valuesMap.put("app21", "api-lazlo-app21");
		valuesMap.put("app43", "api-lazlo-app43");
		valuesMap.put("app28", "api-lazlo-app28");
		valuesMap.put("app27", "api-lazlo-app27");
		valuesMap.put("app26", "api-lazlo-app26");
		valuesMap.put("app25", "api-lazlo-app25");
		valuesMap.put("app31", "api-lazlo-app31");
		valuesMap.put("app30", "api-lazlo-app30");
		host.setValuesToReadableStrings(valuesMap);
		return host;
	}

	private DashboardParameterValue createColoVariable() {
		DashboardParameterValue colo = new DashboardParameterValue();

		colo.setParameterType(ParameterTypeEnum.LIST);
		colo.setLabel("colo");
		colo.setDefaultValue("SNC1");

		HashMap<String, String> valuesMap = new HashMap();
		valuesMap.put("DUB1", ".DUB1");
		valuesMap.put("SAC1", ".SAC1");
		valuesMap.put("SNC1", ".SNC1");
		colo.setValuesToReadableStrings(valuesMap);

		return colo;
	}

	private String getGraphDefinition(GrapherDashboardGraph grapherDashboardGraph) throws IOException {

		String graph = grapherDashboardGraph.getGraph();
		graph = graph.substring(1);

		int ind = graph.indexOf("_");
		if (ind < 0) {
			return null;
		}
		int tmpId = 0;
		try {
			tmpId = Integer.parseInt(graph.substring(0, ind));
		} catch (NumberFormatException e) {
			return null;
		}

		int userId = tmpId;
		String name = graph.substring(ind + 1);

		List<GrapherChartDefinition> definitions = getAllGraphDefinitions(properties);

		GrapherChartDefinition grapherChartDefinition = definitions.stream().filter(def -> def.getUserId() == userId && def.getName().equals(name)).findAny().orElse(null);

		if (grapherChartDefinition != null) {
			return grapherChartDefinition.getData();
		} else {
			return null;
		}
	}
}
