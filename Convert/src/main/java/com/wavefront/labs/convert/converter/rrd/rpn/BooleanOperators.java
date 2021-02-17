package com.wavefront.labs.convert.converter.rrd.rpn;

import java.util.Deque;

public class BooleanOperators {

	public static String lessThan(Deque queue) {
		return genericOperator(queue, "<");
	}

	public static String lessEqual(Deque queue) {
		return genericOperator(queue, "<=");
	}

	public static String greaterThan(Deque queue) {
		return genericOperator(queue, ">");
	}

	public static String greaterEqual(Deque queue) {
		return genericOperator(queue, ">=");
	}

	public static String equal(Deque queue) {
		return genericOperator(queue, "=");
	}

	public static String notEqual(Deque queue) {
		return genericOperator(queue, "!=");
	}

	public static String isUnknown(Deque queue) {
		String ts;
		ts = "if(default(0, " + queue.pop() + ") = 0, 1, 0)";
		queue.push(ts);
		return null;
	}

	public static String _if(Deque<String> queue) {
		String ts;
		String falseValue = queue.pop();
		String trueValue = queue.pop();
		String condition = queue.pop();
		ts = "if(" + condition + ", " + trueValue + ", " + falseValue + ")";
		queue.push(ts);
		return null;
	}

	private static String genericOperator(Deque<String> queue, String operator) {
		String ts;
		String right = queue.pop();
		String left = queue.pop();
		ts = "if(" + left + " " + operator + " " + right + ", 1, 0)";
		queue.push(ts);
		return null;
	}
}
