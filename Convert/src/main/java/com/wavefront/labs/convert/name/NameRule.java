package com.wavefront.labs.convert.name;

import java.util.regex.Pattern;

public class NameRule {

	private String rule;
	private String match;
	private String search;
	private String replace;

	private Pattern matchPattern;
	private Pattern searchPattern;

	public NameRule() {
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
		matchPattern = Pattern.compile(match);

	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
		searchPattern = Pattern.compile(search);
	}

	public String getReplace() {
		return replace;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

	public Pattern getMatchPattern() {
		return matchPattern;
	}

	public Pattern getSearchPattern() {
		return searchPattern;
	}
}
