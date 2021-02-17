package com.wavefront.labs.convert.converter.datadog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavefront.labs.convert.converter.datadog.models.*;
import com.wavefront.rest.models.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DatadogAlertConverter extends AbstractDatadogConverter {
	private static final Logger logger = LogManager.getLogger(DatadogAlertConverter.class);

	private DatadogAlert datadogAlert;

	@Override
	public void parse(Object data) throws IOException {
		if (data instanceof String) {
			parseFromId(data.toString());
		} else if (data instanceof File) {
			parseFromFile((File) data);
		}
	}

	private void parseFromId(String id) throws IOException {
		String url = getBaseApiUrl("monitor/" + id);
		ObjectMapper mapper = new ObjectMapper();
		datadogAlert = mapper.convertValue(mapper.readTree(new URL(url)), DatadogAlert.class);
	}

	private void parseFromFile(File file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		datadogAlert = mapper.convertValue(mapper.readTree(file).get("dash"), DatadogAlert.class);
	}

	@Override
	public List<Object> convert() {

		logger.info("Converting Datadog Alert: " + datadogAlert.getId() + "/" + datadogAlert.getName());

		Alert alert = new Alert();

		alert.setName(datadogAlert.getName());
		alert.setCondition(expressionBuilder.buildExpression(datadogAlert.getQuery()));

		List<Object> models = new ArrayList<>();
		models.add(alert);
		return models;
	}
}
