package com.wavefront.labs.convert.converter.rrd.rpn;

import java.util.Deque;

public class SpecialValues {

	public static String zero(Deque queue) {
		queue.push("0");
		return "function is not supported, 0 will be used";
	}

	public static String one(Deque queue) {
		queue.push("1");
		return "function is not supported, 1 will be used";
	}

	public static String negone(Deque queue) {
		queue.push("-1");
		return "function is not supported, -1 will be used";
	}
}
