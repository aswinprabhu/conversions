package com.wavefront.labs.convert.name;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NameProcessor {

	private HashMap<String, HashMap<String, String>> maps = new HashMap();
	private HashMap<String, List<NameRule>> rules = new HashMap();

	public HashMap<String, HashMap<String, String>> getMaps() {
		return maps;
	}

	public void setMaps(HashMap<String, HashMap<String, String>> maps) {
		this.maps = maps;
	}

	public HashMap<String, List<NameRule>> getRules() {
		return rules;
	}

	public void setRules(HashMap<String, List<NameRule>> rules) {
		this.rules = rules;
	}

	public HashMap<String, String> getMap(String type) {
		if (!maps.containsKey(type)) {
			maps.put(type, new HashMap());
		}
		return maps.get(type);
	}

	public List<NameRule> getRules(String type) {
		if (!rules.containsKey(type)) {
			rules.put(type, new ArrayList());
		}
		return rules.get(type);
	}
}
