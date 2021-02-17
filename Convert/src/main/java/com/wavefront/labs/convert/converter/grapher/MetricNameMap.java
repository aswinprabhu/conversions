package com.wavefront.labs.convert.converter.grapher;

import com.wavefront.labs.convert.converter.grapher.MetricNameMap.Entry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class MetricNameMap extends HashMap<String, Entry> {

	public void load(File file) throws IOException {
		List<String> lines = Files.readAllLines(file.toPath());

		for (String line : lines) {
			Entry entry = new Entry(line, true);
			put(entry.getGrapherName(), entry);
		}
	}

	public class Entry {
		private String originalName;
		private String grapherName;
		private String wavefrontName;

		Entry(String name, boolean fromWF) {
			if (fromWF) {
				wavefrontName = name;
				originalName = name.replaceAll("\\.", "/");
			} else {
				originalName = name;
				wavefrontName = originalName.replaceAll("/", ".");
			}

			grapherName = originalName.replaceAll("[^a-zA-Z0-9\\-_]+", "_");
		}

		public String getOriginalName() {
			return originalName;
		}

		public String getGrapherName() {
			return grapherName;
		}

		public String getWavefrontName() {
			return wavefrontName;
		}
	}
}
