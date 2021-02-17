package com.wavefront.labs.convert.converter.rrd.rpn;

import java.util.Deque;

public class Variables {

	public static String first(Deque queue) {
		String ts;
		ts = "at(\"start\", " + queue.pop() + ")";
		queue.push(ts);
		return null;
	}

	public static String last(Deque queue) {
		String ts;
		ts = "at(\"end\", " + queue.pop() + ")";
		queue.push(ts);
		return null;
	}

	public static String total(Deque queue) {
		String ts;
		ts = "at(\"end\", integral(" + queue.pop() + "))";
		queue.push(ts);
		return null;
	}
}
