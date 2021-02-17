package com.wavefront.labs.convert.converter.rrd.rpn;

import java.util.Deque;

public class ComparingValues {

	public static String min(Deque queue) {
		String ts;
		ts = "min(" + queue.pop() + ", " + queue.pop() + ")";
		queue.push(ts);
		return null;
	}

	public static String max(Deque queue) {
		String ts;
		ts = "max(" + queue.pop() + ", " + queue.pop() + ")";
		queue.push(ts);
		return null;
	}

	public static String between(Deque<String> queue) {
		String ts;
		String upper = queue.pop();
		String lower = queue.pop();
		String expression = queue.pop();
		ts = "between(" + expression + ", " + lower + ", " + upper + ")";
		queue.push(ts);
		return null;
	}
}
