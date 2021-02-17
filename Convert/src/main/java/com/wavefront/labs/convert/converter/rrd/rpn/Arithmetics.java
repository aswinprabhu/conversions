package com.wavefront.labs.convert.converter.rrd.rpn;

import java.util.Deque;

public class Arithmetics {

	public static String add(Deque queue) {
		return genericOperator(queue, "+");
	}

	public static String substract(Deque queue) {
		return genericOperator(queue, "-");
	}

	public static String multiply(Deque queue) {
		return genericOperator(queue, "*");
	}

	public static String divide(Deque queue) {
		return genericOperator(queue, "/");
	}

	public static String sin(Deque queue) {
		return genericFunctionWrap(queue, "sin");
	}

	public static String cos(Deque queue) {
		return genericFunctionWrap(queue, "cos");
	}

	public static String log(Deque queue) {
		return genericFunctionWrap(queue, "log");
	}

	public static String exp(Deque queue) {
		return genericFunctionWrap(queue, "exp");
	}

	public static String sqrt(Deque queue) {
		return genericFunctionWrap(queue, "sqrt");
	}

	public static String atan(Deque queue) {
		return genericFunctionWrap(queue, "atan");
	}

	public static String atan2(Deque queue) {
		return genericFunctionWrap(queue, "atan2");
	}

	public static String floor(Deque queue) {
		return genericFunctionWrap(queue, "floor");
	}

	public static String ceil(Deque queue) {
		return genericFunctionWrap(queue, "ceil");
	}

	public static String deg2rad(Deque queue) {
		return genericFunctionWrap(queue, "deg2rad");
	}

	public static String rad2deg(Deque queue) {
		return genericFunctionWrap(queue, "rad2deg");
	}

	public static String abs(Deque queue) {
		return genericFunctionWrap(queue, "abs");
	}

	private static String genericOperator(Deque<String> queue, String operator) {
		String ts;
		String right = queue.pop();
		String left = queue.pop();
		ts = "(" + left + " " + operator + " " + right + ")";
		queue.push(ts);
		return null;
	}

	private static String genericFunctionWrap(Deque queue, String functionWrap) {
		String ts;
		ts = functionWrap + "(" + queue.pop() + ")";
		queue.push(ts);
		return null;
	}

}
