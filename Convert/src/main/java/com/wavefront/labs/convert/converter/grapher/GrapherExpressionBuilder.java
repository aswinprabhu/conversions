package com.wavefront.labs.convert.converter.grapher;

import com.wavefront.labs.convert.converter.rrd.RRDExpressionBuilder;
import com.wavefront.labs.convert.converter.rrd.models.Def;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class GrapherExpressionBuilder extends RRDExpressionBuilder {
	private static final Logger logger = LogManager.getLogger(GrapherExpressionBuilder.class);

	private static MetricNameMap metricNameMap = null;

	@Override
	public void init(Properties properties) {
		super.init(properties);

		if (metricNameMap == null) {
			metricNameMap = new MetricNameMap();
			try {
				metricNameMap.load(new File(properties.getProperty("grapher.definitions.metrics")));
			} catch (IOException e) {
				logger.error("Could not load Metrics definitions", e);
			}
		}
	}

	@Override
	public String buildMetricName(String orig) {
		// get a clean name first (no special chars)
		String tmp = super.buildMetricName(orig);

		String metricName = "";
		if (metricNameMap.containsKey(tmp)) {
			metricName = metricNameMap.get(tmp).getWavefrontName();

		} else {
			// parse a Grapher metric name, into what a Wavefront metric name would be if not found in metricNameMap
			// 1. split on __  For each odd instance replace with .@  For even replace with @.
			// 2. replace all _ with a .
			// 3. replace all @ with a _

			String[] doubleUnderParts = tmp.split("__");
			for (int i = 0; i < doubleUnderParts.length; i++) {
				if (i == doubleUnderParts.length - 1) {
					metricName += doubleUnderParts[i];
				} else {
					if ((i + 1) % 2 == 0) {
						metricName += doubleUnderParts[i] + "@.";
					} else {
						metricName += doubleUnderParts[i] + ".@";
					}
				}
			}

			metricName = metricName.replaceAll("_", ".");
			metricName = metricName.replaceAll("@", "_");
		}

		return metricName;
	}

	@Override
	public String buildExpression(Object o) {
		Def def = (Def) o;

		String rrdFile = def.getRRDFile();
		String CF = def.getCF();

		int pos1 = rrdFile.lastIndexOf("/");
		int pos2 = rrdFile.lastIndexOf(".");
		String base = rrdFile.substring(pos1 + 1, pos2);
		String metricName = buildMetricName(base);

		String ts = "";
		String filter = properties.getProperty("grapher.filter@Current", "");
		if (!filter.equals("")) {
			ts = "ts(\"" + metricName + "\", source=" + filter + ")";
		} else {
			ts = "ts(\"" + metricName + "\")";
		}

		return ts;
	}

}
