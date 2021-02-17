package com.wavefront.labs.convert.converter.rrd.rpn;

import java.util.Arrays;
import java.util.Deque;

public class SetOperations {

	public static String sort(Deque<String> queue) {
		int count;
		try {
			count = Integer.parseInt(queue.pop());
		} catch (NumberFormatException e) {
			return "could not process count as integer";
		}

		Double[] values = new Double[count];
		boolean validValues = true;
		for (int i = 0; i < count; i++) {
			try {
				values[i] = Double.valueOf(queue.pop());
			} catch (NumberFormatException e) {
				validValues = false;
				break;
			}
		}

		if (validValues) {
			Arrays.sort(values);
			for (int i = count - 1; i <= 0; i--) {
				queue.push(values[i].toString());
			}

		} else {
			for (int i = count - 1; i <= 0; i--) {
				if (values[i] != null) {
					queue.push(values[i].toString());
				}
			}
			return "could not sort do to a non numeric value";
		}

		return null;
	}

	public static String rev(Deque<String> queue) {
		int count = queue.size();
		String[] values = new String[count];

		for (int i = 0; i < count; i++) {
			values[i] = queue.pop();
		}

		for (int i = 0; i < count; i++) {
			queue.push(values[i]);
		}

		return null;
	}

	public static String avg(Deque<String> queue) {
		String ts;
		int count;
		try {
			count = Integer.parseInt(queue.pop());
		} catch (NumberFormatException e) {
			return "could not process count as integer";
		}

		ts = "(";
		for (int i = 0; i < count; i++) {
			if (i > 0) {
				ts += " + ";
			}
			ts += "(" + queue.pop() + ")";
		}
		ts += ") / " + count;
		queue.push(ts);
		return null;
	}

	public static String median(Deque<String> queue) {
		avg(queue);
		return "function not supported, using equivalent to AVG instead";
	}

	public static String trend(Deque queue) {
		String ts;
		ts = "mavg(" + queue.pop() + "s, " + queue.pop() + ")";
		queue.push(ts);
		return null;
	}

	public static String predict(Deque<String> queue) {
		String expression = queue.pop();
		int window;
		try {
			window = Integer.parseInt(queue.pop());
		} catch (NumberFormatException e) {
			return "could not process window as integer";
		}
		int count;
		try {
			count = Integer.parseInt(queue.pop());
		} catch (NumberFormatException e) {
			return "could not process count as integer";
		}

		if (count > 0) {
			return explicitPredict(queue, expression, window, count);
		} else {
			return shiftPredict(queue, expression, window, count);
		}
	}

	private static String explicitPredict(Deque<String> queue, String expression, int window, int count) {

		String ts;
		ts = "(";

		for (int i = 0; i < count; i++) {
			if (i > 0) {
				ts += " + ";
			}

			int shift;
			try {
				shift = Integer.parseInt(queue.pop());
			} catch (NumberFormatException e) {
				return "could not process shift as integer";
			}

			ts += "lag(" + shift + "s, " + expression + ")";
			ts += " + ";
			ts += "lag(" + (shift + window) + "s, " + expression + ")";
		}

		ts += ") / " + (count * 2);
		queue.push(ts);
		return null;
	}

	private static String shiftPredict(Deque<String> queue, String expression, int window, int count) {
		count *= -1;
		int shiftVal;
		try {
			shiftVal = Integer.parseInt(queue.pop());
		} catch (NumberFormatException e) {
			return "could not process shift as integer";
		}

		String ts;
		ts = "(";

		for (int i = 0; i < count; i++) {
			if (i > 0) {
				ts += " + ";
			}

			int shift = shiftVal * (i + 1);

			ts += "lag(" + shift + "s, " + expression + ")";
			ts += " + ";
			ts += "lag(" + (shift + window) + "s, " + expression + ")";
		}

		ts += ") / " + (count * 2);
		queue.push(ts);
		return null;
	}
}
