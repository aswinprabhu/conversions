package com.wavefront.labs.convert.converter.datadog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class DatadogApiConverter extends AbstractDatadogConverter {
	private static final Logger logger = LogManager.getLogger(DatadogApiConverter.class);

	private List<DatadogTimeboardConverter> converters;

	@Override
	public void init(Properties properties) {
		super.init(properties);
		converters = new ArrayList();
	}

	@Override
	public void parse(Object data) throws IOException {

		String apiKey = properties.getProperty("datadog.api.key");
		String applicationKey = properties.getProperty("datadog.application.key");
		String url = "https://app.datadoghq.com/api/v1/dash";
		url += "?api_key=" + apiKey;
		url += "&application_key=" + applicationKey;

		try {
			ObjectMapper mapper = new ObjectMapper();
			List<HashMap> dashes = mapper.convertValue(mapper.readTree(new URL(url)).get("dashes"), new TypeReference<List<HashMap>>() {
			});
			processDashes(dashes);
		} catch (IOException e) {
			logger.error("Could not get list of available timeboards via API", e);
		}
	}

	private void processDashes(List<HashMap> dashes) throws IOException {

		String titleMatch = properties.getProperty("datadog.timeboard.titleMatch", ".*");
		Pattern titlePattern = Pattern.compile(titleMatch);

		for (HashMap dash : dashes) {
			if (dash.containsKey("id")) {

				String title = dash.get("title").toString();
				if (titlePattern.matcher(title).matches()) {
					DatadogTimeboardConverter converter = new DatadogTimeboardConverter();
					converter.init(properties);
					converter.parse(dash.get("id").toString());
					if (!converter.getParsingErrorFlag()) {
						converters.add(converter);
					} else {
						com.wavefront.labs.convert.utils.Tracker.addToList("\"Dashboard Parsing Error\"", "\"Dashboard (id: "+dash.get("id").toString()+" | title: "+dash.get("title").toString()+")\"");
						com.wavefront.labs.convert.utils.Tracker.increment("\"Dashboard Parsing Error Count\"");
					}
				}
			}
		}

	}

	@Override
	public List convert() {

		List models = new ArrayList();

		for (DatadogTimeboardConverter converter : converters) {
			try {
				models.addAll(converter.convert());
				com.wavefront.labs.convert.utils.Tracker.increment("\"DatadogTimeboardConverter::convert Successful (Count)\"");
			} catch (Exception ex) {
				logger.error("Exception during convert", ex);
				com.wavefront.labs.convert.utils.Tracker.increment("\"DatadogTimeboardConverter::convert Exception (Count)\"");
			}
		}

		return models;

	}
}
