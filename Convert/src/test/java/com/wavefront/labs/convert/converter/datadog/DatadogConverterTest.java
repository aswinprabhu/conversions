package com.wavefront.labs.convert.converter.datadog;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatadogConverterTest {
	@Test
	public void parseString() {
	}

	@Test
	public void parseFile() throws Exception {
		Properties properties = new Properties();
		properties.load(new FileReader(new File("datadog.properties")));

		DatadogConverter datadogConverter = new DatadogConverter();
		datadogConverter.init(properties);

		List<Path> paths = Files.list(new File("test_resources/datadog").toPath()).collect(Collectors.toList());
		for (Path path : paths) {
			File _file = path.toFile();

			if (!_file.isDirectory()) {
				datadogConverter.parse(_file);
			}
		}

	}

	@Test
	public void convert() throws Exception {
		Properties properties = new Properties();
		properties.load(new FileReader(new File("datadog.properties")));

		DatadogConverter datadogConverter = new DatadogConverter();
		datadogConverter.init(properties);

		List<Path> paths = Files.list(new File("test_resources/datadog").toPath()).collect(Collectors.toList());
		for (Path path : paths) {
			File _file = path.toFile();

			if (!_file.isDirectory()) {
				datadogConverter.parse(_file);
			}
		}
		
		List<Object> models = datadogConverter.convert();
	}

}