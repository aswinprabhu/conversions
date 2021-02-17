package com.wavefront.labs.convert.converter.grafana;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class GrafanaConverterTest {
	@Test
	public void parse() throws Exception {
		Properties properties = new Properties();
		properties.load(new FileReader(new File("grafana.properties")));

		GrafanaConverter grafanaConverter = new GrafanaConverter();
		grafanaConverter.init(properties);


		List<Path> paths = Files.list(new File("test_resources/grafana").toPath()).collect(Collectors.toList());
		for (Path path : paths) {
			File _file = path.toFile();

			if (!_file.isDirectory()) {
				grafanaConverter.parse(_file);
			}
		}
	}

	@Test
	public void convert() throws Exception {

		Properties properties = new Properties();
		properties.load(new FileReader(new File("grafana.properties")));

		GrafanaConverter grafanaConverter = new GrafanaConverter();
		grafanaConverter.init(properties);

		List<Path> paths = Files.list(new File("test_resources/grafana").toPath()).collect(Collectors.toList());
		for (Path path : paths) {
			File _file = path.toFile();

			if (!_file.isDirectory()) {
				grafanaConverter.parse(_file);
			}
		}
		
		List<Object> models = grafanaConverter.convert();
		System.out.println(models.size());

	}

}