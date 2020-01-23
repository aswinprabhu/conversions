package com.wavefront.labs.convert.converter.datadog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavefront.labs.convert.converter.datadog.models.DatadogAlert;
import com.wavefront.labs.convert.converter.wavefront.models.AlertTarget;
import com.wavefront.rest.models.TargetInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DatadogAlertTargetConverter extends AbstractDatadogConverter {
	private static final Logger logger = LogManager.getLogger(DatadogAlertTargetConverter.class);

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

		logger.info("Converting Datadog AlertTarget: " + datadogAlert.getId() + "/" + datadogAlert.getName());

		AlertTarget alertTarget = new AlertTarget();

		alertTarget.setName(datadogAlert.getName());

		List<Object> models = new ArrayList<>();
		models.add(alertTarget);
		return models;
	}
}
