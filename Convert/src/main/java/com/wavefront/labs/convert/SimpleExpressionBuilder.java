package com.wavefront.labs.convert;

import java.util.Properties;

public class SimpleExpressionBuilder implements ExpressionBuilder {

	@Override
	public void init(Properties properties) {
	}

	@Override
	public String buildMetricName(String orig) {
		return null;
	}

	@Override
	public String buildExpression(Object data) {
		return data.toString();
	}
}
