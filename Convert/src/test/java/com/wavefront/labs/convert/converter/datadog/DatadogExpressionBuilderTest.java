package com.wavefront.labs.convert.converter.datadog;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatadogExpressionBuilderTest {
	@Test
	public void buildExpression() {

		Properties properties = new Properties();
		properties.setProperty("datadog.replaceUnderscore", ".");

		DatadogExpressionBuilder ddeb = new DatadogExpressionBuilder();
		ddeb.init(properties);

		assertEquals("sum(ts(\"haproxy.count.per.status\", status=\"available\"))",
				ddeb.buildExpression("sum:haproxy.count_per_status{status:available}"));

		assertEquals("if(bottom(5, top(10, mmax(1h, max(ts(\"system.cpu.iowait\"), sources))), max(ts(\"system.cpu.iowait\"), sources))",
				ddeb.buildExpression("top_offset(max:system.cpu.iowait{*} by {host}, 5, 'max', 'desc', 5)"));

		assertEquals("wmavg(20m, abs(align(50s, mean, default(0, max(ts(\"system.cpu.idle\", env=\"dev\" and availability-zone=\"us-west-1a\"), sources)))))",
				ddeb.buildExpression("ewma_20(abs(max:system.cpu.idle{env:dev,availability-zone:us-west-1a} by {host}.fill(zero).rollup(avg, 50)))"));

		assertEquals("abs(if(top(5, mavg(1h, align(50s, mean, default(0, max(ts(\"system.cpu.idle\", env=\"dev\" and availability-zone=\"us-west-1a\"), sources))))), align(50s, mean, default(0, max(ts(\"system.cpu.idle\", env=\"dev\" and availability-zone=\"us-west-1a\"), sources)))))",
				ddeb.buildExpression("abs(top(max:system.cpu.idle{env:dev,availability-zone:us-west-1a} by {host}.fill(zero).rollup(avg, 50), 5, 'mean', 'desc'))"));

		assertEquals("avg(max(sum(abs(if(top(5, mavg(1h, align(50s, mean, default(0, avg(ts(\"system.cpu.idle\", env=\"dev\" and availability-zone=\"us-west-1a\"), sources))))), align(50s, mean, default(0, avg(ts(\"system.cpu.idle\", env=\"dev\" and availability-zone=\"us-west-1a\"), sources))))))))",
				ddeb.buildExpression("avg(max(sum(abs(top(avg:system.cpu.idle{env:dev,availability-zone:us-west-1a} by {host}.fill(zero).rollup(avg, 50), 5, 'mean', 'desc')))))"));

		assertEquals("avg(ts(\"system.load.1\"))",
				ddeb.buildExpression("avg:system.load.1{*}"));

		assertEquals("avg(ts(\"system.load.1\")) *  2  + max(ts(\"system.load.1\"))",
				ddeb.buildExpression("avg:system.load.1{*}*2+max:system.load.1{*}"));

		assertEquals(" 100  - avg(ts(\"system.cpu.idle\", env=\"prod\" and product=\"symphony-ui\"), sources)",
				ddeb.buildExpression("100-avg:system.cpu.idle{env:prod,product:symphony-ui} by {host}"));

		assertEquals("if(bottom(5, top(10, mmax(1h, ts(\"system.cpu.iowait\"))), ts(\"system.cpu.iowait\"))",
				ddeb.buildExpression("top_offset(system.cpu.iowait{*} by {host}, 5, 'max', 'desc', 5)"));

		assertEquals("ts(\"system.load.1\")",
				ddeb.buildExpression("system.load.1{*}"));

		assertEquals("ts(\"system.load.1\")",
				ddeb.buildExpression("system.load.1{$scope}"));

		assertEquals("ts(\"system.load.1\", env=\"prod\" and not app_name=\"app1\")",
				ddeb.buildExpression("system.load.1{env:prod,!app_name:app1}"));

		assertEquals("",
						ddeb.buildExpression(""));


	}

}