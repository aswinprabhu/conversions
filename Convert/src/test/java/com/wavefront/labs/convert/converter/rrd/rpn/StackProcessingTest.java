package com.wavefront.labs.convert.converter.rrd.rpn;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackProcessingTest {
	Deque<String> queue;


	@BeforeEach
	public void beforeEach() {
		queue = new ArrayDeque();
		queue.push("a");
		queue.push("b");
		queue.push("c");
		queue.push("d");
	}

	@Test
	public void dup() {

		StackProcessing.dup(queue);

		assertEquals("d", queue.pop());
		assertEquals("d", queue.pop());
	}

	@Test
	public void pop() {
		StackProcessing.pop(queue);

		assertEquals("c", queue.pop());
	}

	@Test
	public void exc() {

		StackProcessing.exc(queue);

		assertEquals("c", queue.pop());
		assertEquals("d", queue.pop());

	}

	@Test
	public void depth() {
		StackProcessing.depth(queue);

		assertEquals("4", queue.pop());
	}

	@Test
	public void copy() {

		queue.push("2");

		StackProcessing.copy(queue);

		assertEquals("d", queue.pop());
		assertEquals("c", queue.pop());
		assertEquals("d", queue.pop());
		assertEquals("c", queue.pop());
	}

	@Test
	public void index() {
		queue.push("3");

		StackProcessing.index(queue);

		assertEquals("b", queue.pop());
	}

	@Test
	public void roll() {

	}

}