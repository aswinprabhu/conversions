package com.wavefront.labs.convert;

import java.util.Properties;

public interface ExpressionBuilder {

	void init(Properties properties);

	String buildMetricName(String orig);

	String buildExpression(Object data);

}
