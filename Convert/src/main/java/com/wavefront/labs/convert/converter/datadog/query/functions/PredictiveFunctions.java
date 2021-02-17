package com.wavefront.labs.convert.converter.datadog.query.functions;

import com.wavefront.labs.convert.converter.datadog.query.DatadogFunction;

public class PredictiveFunctions {
    public static String forecast(DatadogFunction function) {
        return "nnforecast(1d, " + function.getQuery() + ")";
    }
}
