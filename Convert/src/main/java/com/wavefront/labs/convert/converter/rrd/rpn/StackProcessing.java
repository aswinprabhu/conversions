package com.wavefront.labs.convert.converter.rrd.rpn;

import java.util.Deque;

public class StackProcessing {

	public static String dup(Deque queue) {
		queue.push(queue.peek());
		return null;
	}

	public static String pop(Deque queue) {
		queue.pop();
		return null;
	}

	public static String exc(Deque<String> queue) {
		String first = queue.pop();
		String next = queue.pop();
		queue.push(first);
		queue.push(next);
		return null;
	}

	public static String depth(Deque<String> queue) {
		queue.push(String.valueOf(queue.size()));
		return null;
	}

	public static String copy(Deque<String> queue) {
		int count;
		try {
			count = Integer.parseInt(queue.pop());
		} catch (NumberFormatException e) {
			return "could not process count as integer";
		}

		String[] values = new String[count];
		for (int i = 0; i < count; i++) {
			values[i] = queue.pop();
		}

		for (int n = 0; n < 2; n++) {
			for (int i = count - 1; i >= 0; i--) {
				queue.push(values[i]);
			}
		}
		return null;
	}

	public static String index(Deque<String> queue) {
		int index;
		try {
			index = Integer.parseInt(queue.pop());
		} catch (NumberFormatException e) {
			return "could not process index as integer";
		}

		String newVal = null;
		int n = 0;
		for (String item : queue) {
			n++;
			if (n >= index) {
				newVal = item;
				break;
			}
		}

		if (newVal != null) {
			queue.push(newVal);
		}

		return null;
	}

	public static String roll(Deque<String> queue) {
		return "function not yet supported, may be added in future";
	}
}
