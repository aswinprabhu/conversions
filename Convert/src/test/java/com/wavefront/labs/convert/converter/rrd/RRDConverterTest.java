package com.wavefront.labs.convert.converter.rrd;

import com.wavefront.rest.models.Chart;
import com.wavefront.rest.models.ChartSourceQuery;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class RRDConverterTest {

	@Test
	public void fullTest() throws IOException {
		RRDConverter rrdConverter = new RRDConverter();

		List<Path> paths = Files.list(new File("test_resources/rrd").toPath()).collect(Collectors.toList());
		for (Path path : paths) {
			File _file = path.toFile();

			if (!_file.isDirectory()) {
				rrdConverter.parse(_file);
			}
		}

		List<Object> models = rrdConverter.convert();

		for (Object model : models) {
			if (model instanceof Chart) {
				Chart chart = (Chart) model;
				for (ChartSourceQuery query : chart.getSources()) {
					System.out.println(query.getName() + " --> " + query.getQuery());
				}
			}
		}
	}

}