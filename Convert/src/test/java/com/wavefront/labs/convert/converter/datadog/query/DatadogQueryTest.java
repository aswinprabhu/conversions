package com.wavefront.labs.convert.converter.datadog.query;

import org.junit.jupiter.api.Test;

public class DatadogQueryTest {

	@Test
	public void debugTest() {

		System.out.println();

		System.out.println(new DatadogQuery("sum:haproxy.count_per_status{status:available}").toString());
		System.out.println(new DatadogQuery("top_offset(max:system.cpu.iowait{*} by {host}, 5, 'max', 'desc', 5)").toString());
		System.out.println(new DatadogQuery("ewma_20(abs(max:system.cpu.idle{env:dev,availability-zone:us-west-1a} by {host}.fill(zero).rollup(avg, 50)))").toString());
		System.out.println(new DatadogQuery("abs(top(max:system.cpu.idle{env:dev,availability-zone:us-west-1a} by {host}.fill(zero).rollup(avg, 50), 5, 'mean', 'desc'))").toString());
		System.out.println(new DatadogQuery("avg(max(sum(abs(top(avg:system.cpu.idle{env:dev,availability-zone:us-west-1a} by {host}.fill(zero).rollup(avg, 50), 5, 'mean', 'desc')))))").toString());
		System.out.println(new DatadogQuery(("avg:system.load.1{*}")).toString());
		System.out.println(new DatadogQuery(""));
	}



}