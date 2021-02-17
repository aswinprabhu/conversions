package com.wavefront.labs.convert.converter.rrd.models;

import com.wavefront.labs.convert.converter.rrd.RRDContext;

import java.util.ArrayList;

public abstract class Definition {

	protected String variableName;
	protected String expression;
	protected ArrayList<String> warnings;
	public Definition(String line) {
		int colon = line.indexOf(":");
		int eq = line.indexOf("=", colon);

		variableName = stripQuotes(line.substring(colon + 1, eq));
		expression = stripQuotes(line.substring(eq + 1));
		warnings = new ArrayList();
	}

	public abstract String calculate(RRDContext RRDContext);

	protected String stripQuotes(String value) {
		if (value.startsWith("'")) {
			value = value.substring(1);
		}

		if (value.endsWith(("'"))) {
			value = value.substring(0, value.length() - 1);
		}

		return value;
	}

	public String getVariableName() {
		return variableName;
	}

	public ArrayList<String> getWarnings() {
		return warnings;
	}
}
