# Datadog Converter

The Datadog converter can optionally use the Datadog API to find all Timeboards.  
An optional regular expression can be used to convert specific Timeboards only.

## Datadog Properties
Core properties should have the following values
- `convert.converter=com.wavefront.labs.convert.converter.datadog.DatadogConverter`
- `convert.expressionBuilder=com.wavefront.labs.convert.converter.datadog.DatadogExpressionBuilder`

In addition to the Core properties you will also need the following
- `datadog.api.key`: Datadog API key. (required only if using Datadog API) 
- `datadog.application.key`: Datadog Application Key (required only if using Datadog API)
- `datadog.timeboard.enabled`: <true | false> to convert Timeboards (default: true)  
- `datadog.timeboard.titleMatch`: optional regex to use when finding Timeboards to convert 
- `datadog.alert.enabled`: <true | false> to convert Alerts (default: true)  
- `datadog.alert.titleMatch`: optional regex to use when finding Alerts to convert 
- `datadog.underscoreReplace=`: Wavefront UI uses a `.` to create metric hierarchies 
- `datadog.dropTags=role,stackname,stack`: optional list of tags to drop from DataDog