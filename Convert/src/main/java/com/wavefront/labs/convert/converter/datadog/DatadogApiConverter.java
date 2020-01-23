package com.wavefront.labs.convert.converter.datadog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavefront.labs.convert.Utils.Tracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class DatadogApiConverter extends AbstractDatadogConverter {
	private static final Logger logger = LogManager.getLogger(DatadogApiConverter.class);

	private List<DatadogTimeboardConverter> dashboardConverters;
	private List<DatadogAlertTargetConverter> alertTargetConverters;
	private List<DatadogAlertConverter> alertConverters;

	@Override
	public void init(Properties properties) {
		super.init(properties);
		dashboardConverters = new ArrayList<>();
		alertTargetConverters = new ArrayList<>();
		alertConverters = new ArrayList<>();
	}

	@Override
	public void parse(Object data) throws IOException {

		if (Boolean.parseBoolean(properties.getProperty("datadog.timeboard.enabled", "true"))) {
			String url = getBaseApiUrl("dashboard");
			ObjectMapper mapper = new ObjectMapper();
			List<HashMap<Object, Object>> dashes = mapper.convertValue(mapper.readTree(new URL(url)).get("dashboards"), new TypeReference<List<HashMap<Object, Object>>>() {
			});
			processDashboards(dashes);
		}

		if (Boolean.parseBoolean(properties.getProperty("datadog.alert.enabled", "true")) || Boolean.parseBoolean(properties.getProperty("datadog.alertTarget.enabled", "true"))) {
			String url = getBaseApiUrl("monitor");
			ObjectMapper mapper = new ObjectMapper();
			List<HashMap<Object, Object>> alerts = mapper.convertValue(mapper.readTree(new URL(url)), new TypeReference<List<HashMap<Object, Object>>>() {
			});
			if (Boolean.parseBoolean(properties.getProperty("datadog.alert.enabled", "true"))) {
				processAlerts(alerts);
			}
			if (Boolean.parseBoolean(properties.getProperty("datadog.alertTarget.enabled", "true"))) {
				processAlertTargets(alerts);
			}
		}

	}

	private void processDashboards(List<HashMap<Object, Object>> dashes) throws IOException {

		String titleMatch = properties.getProperty("datadog.timeboard.titleMatch", ".*");
		Pattern titlePattern = Pattern.compile(titleMatch);

		for (HashMap<Object, Object> dash : dashes) {
			if (dash.containsKey("id")) {

				String title = dash.get("title").toString();
				if (titlePattern.matcher(title).matches()) {

					DatadogTimeboardConverter converter = new DatadogTimeboardConverter();
					converter.init(properties);
					converter.parse(dash.get("id").toString());
					dashboardConverters.add(converter);
					Tracker.increment("Dashboards Successfully Parsed (count)");
				}
			}
		}

	}

	private void processAlerts(List<HashMap<Object, Object>> alerts) throws IOException {

		String titleMatch = properties.getProperty("datadog.alert.titleMatch", ".*");
		Pattern titlePattern = Pattern.compile(titleMatch);

		for (HashMap<Object, Object> alert : alerts) {
			if (alert.containsKey("id")) {

				String name = alert.get("name").toString();
				if (titlePattern.matcher(name).matches()) {

					DatadogAlertConverter converter = new DatadogAlertConverter();
					converter.init(properties);
					converter.parse(alert.get("id").toString());
					alertConverters.add(converter);
					Tracker.increment("Alerts Successfully Parsed (count)");
				}
			}
		}
	}

	private void processAlertTargets(List<HashMap<Object, Object>> alerts) throws IOException {

		String titleMatch = properties.getProperty("datadog.alert.titleMatch", ".*");
		Pattern titlePattern = Pattern.compile(titleMatch);

		for (HashMap<Object, Object> alert : alerts) {
			if (alert.containsKey("id")) {

				String name = alert.get("name").toString();
				if (titlePattern.matcher(name).matches()) {

					DatadogAlertTargetConverter converter = new DatadogAlertTargetConverter();
					converter.init(properties);
					converter.parse(alert.get("id").toString());
					alertTargetConverters.add(converter);
					Tracker.increment("Alert Targets Successfully Parsed (count)");
				}
			}
		}
	}

	@Override
	public List<Object> convert() {

		List<Object> models = new ArrayList<>();

		if (Boolean.parseBoolean(properties.getProperty("datadog.timeboard.enabled", "true"))) {
			for (DatadogTimeboardConverter converter : dashboardConverters) {
				try {
					models.addAll(converter.convert());
					Tracker.increment("DatadogTimeboardConverter::convert Successful (count)");
				} catch (Exception e) {
					logger.error("Exception during Dashboard convert", e);
					Tracker.increment("DatadogTimeboardConverter::convert Exception (count)");
				}
			}
		}

		if (Boolean.parseBoolean(properties.getProperty("datadog.alertTarget.enabled", "true"))) {
			for (DatadogAlertTargetConverter converter : alertTargetConverters) {
				try {
					models.addAll(converter.convert());
					Tracker.increment("DatadogAlertTargetConverter::convert Successful (count)");
				} catch (Exception e) {
					logger.error("Exception during Alert convert", e);
					Tracker.increment("DatadogAlertTargetConverter::convert Exception (count)");
				}
			}
		}

		if (Boolean.parseBoolean(properties.getProperty("datadog.alert.enabled", "true"))) {
			for (DatadogAlertConverter converter : alertConverters) {
				try {
					models.addAll(converter.convert());
					Tracker.increment("DatadogAlertConverter::convert Successful (count)");
				} catch (Exception e) {
					logger.error("Exception during Alert convert", e);
					Tracker.increment("DatadogAlertConverter::convert Exception (count)");
				}
			}
		}

		return models;

	}

}
