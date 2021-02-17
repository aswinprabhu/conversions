package com.wavefront.labs.convert.converter.rrd.rpn;

import java.util.Deque;

public class Time {

	public static String time(Deque queue) {
		String ts;
		ts = "time()";
		queue.push(ts);
		return null;
	}

	public static String ltime(Deque queue) {
		time(queue);
		return "function is not supported, will use point time instead";
	}
}
