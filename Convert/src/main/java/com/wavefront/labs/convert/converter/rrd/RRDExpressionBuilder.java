package com.wavefront.labs.convert.converter.rrd;

import com.wavefront.labs.convert.DefaultExpressionBuilder;
import com.wavefront.labs.convert.converter.rrd.models.Def;

public class RRDExpressionBuilder extends DefaultExpressionBuilder {

	@Override
	public String buildExpression(Object o) {
		Def def = (Def) o;

		String rrdFile = def.getRRDFile();
		String dsName = def.getDsName();
		String CF = def.getCF();

		int pos1 = rrdFile.lastIndexOf("/");
		int pos2 = rrdFile.lastIndexOf(".");
		String base = rrdFile.substring(pos1 + 1, pos2);
		base = base.replaceAll("_", ".");
		String metricName = base + "." + dsName;

		String ts = super.buildExpression(metricName);

		switch (CF) {
			case "AVERAGE":
				ts = "avg(" + ts + ")";
				break;
			case "MIN":
				ts = "min(" + ts + ")";
				break;
			case "MAX":
				ts = "max(" + ts + ")";
				break;
			default:
				break;
		}

		return ts;

	}
}
