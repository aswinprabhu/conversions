package com.wavefront.labs.convert.converter.rrd;

import com.wavefront.labs.convert.converter.rrd.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Stream;

public class RRDContext {

	private static final Logger logger = LogManager.getLogger(RRDContext.class);

	private Properties properties;
	private ArrayList<Definition> definitions;
	private ArrayList<Graph> graphs;
	private HashMap<String, String> options;

	private HashMap<String, String> variables;

	public RRDContext() {
		properties = new Properties();
		definitions = new ArrayList();
		graphs = new ArrayList();
		options = new HashMap();
		variables = new HashMap();
	}

	public RRDContext(Properties properties) {
		this();
		this.properties = properties;
	}

	public void parse(Object data) throws IOException {
		if (data instanceof String) {
			new BufferedReader(new StringReader(data.toString())).lines().forEach(this::parseLine);

		} else if (data instanceof InputStream) {
			new BufferedReader(new InputStreamReader((InputStream) data)).lines().forEach(this::parseLine);

		} else if (data instanceof File) {
			Path path = ((File) data).toPath();
			try (Stream<String> stream = Files.lines(path)) {
				stream.forEach(this::parseLine);
			}

		} else if (data instanceof Path) {
			try (Stream<String> stream = Files.lines((Path) data)) {
				stream.forEach(this::parseLine);
			}
		} else {
			logger.error("Invalid type to parse: " + data.getClass().getName());
		}
	}

	private void parseLine(String line) {
		Definition definition = null;

		if (line.startsWith("DEF:")) {
			definition = new Def(line);
			definitions.add(definition);

		} else if (line.startsWith("VDEF:")) {
			definition = new VDef(line);
			definitions.add(definition);

		} else if (line.startsWith("CDEF:")) {
			definition = new CDef(line);
			definitions.add(definition);

		} else if (line.startsWith("--")) {
			parseOptionLine(line);

		} else {
			graphs.add(new Graph(line, this));
		}

		if (definition != null) {
			variables.put(definition.getVariableName(), definition.calculate(this));
		}
	}

	private void parseOptionLine(String line) {
		String[] opts = line.split(" ");
		for (int i = 0; i < opts.length; i++) {
			if (opts[i].startsWith("--")) {
				String name = opts[i].substring(2);
				if (i + 1 < opts.length && !opts[i + 1].startsWith("--")) {
					options.put(name, opts[i + 1]);
					i++;
				} else {
					options.put(name, "");
				}
			}
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public boolean hasVariable(String name) {
		return variables.containsKey(name);
	}

	public String getVariable(String name) {
		if (variables.containsKey(name)) {
			return variables.get(name);
		} else {
			return null;
		}
	}

	public HashMap<String, String> getVariables() {
		return variables;
	}

	public ArrayList<Definition> getDefinitions() {
		return definitions;
	}

	public ArrayList<Graph> getGraphs() {
		return graphs;
	}

	public HashMap<String, String> getOptions() {
		return options;
	}
}
