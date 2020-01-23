package com.wavefront.labs.convert;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

	public static String sluggify(String source) {
		return sluggify(source, "-");
	}

	public static String sluggify(String source, String slug) {
		return source.replaceAll("[^a-zA-Z0-9-]", slug).toLowerCase();
	}

	public static class Tracker {
	    public static Map<String, Object> map = new HashMap<>();

	    public static void addToList(String key, String obj) {
	        System.out.println(obj);
	        if (!map.containsKey(key)) {
	            map.put(key, new ArrayList<>());
	        }
	        ((List<Object>) map.get(key)).add(obj);
	    }

	    public static void increment(String key) {
	        if (!map.containsKey(key)) {
	            map.put(key, 1);
	        } else {
	            map.put(key, ((Integer) map.get(key)) + 1);
	        }
	    }

	    public static Object get(String key) {
	    	return map.get(key);
		}

		public static void set(String key, Object value) { map.put(key, value); }

		public static void getSummary() {
			String json = new Gson().toJson(map);
		}
	}
}
