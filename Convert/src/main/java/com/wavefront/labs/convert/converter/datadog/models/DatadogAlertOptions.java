package com.wavefront.labs.convert.converter.datadog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"notify_audit", "locked", "timeout_h", "silenced", "new_host_delay", "notify_no_data", "renotify_interval", "no_data_timeframe", "include_tags", "require_full_window", "escalation_message", "evaluation_delay", "synthetics_check_id", "threshold_windows"})
public class DatadogAlertOptions {
    private DatadogAlertOptionThresholds thresholds;

    public DatadogAlertOptionThresholds getThresholds() { return thresholds; }

    public void setThresholds(DatadogAlertOptionThresholds thresholds) { this.thresholds = thresholds; }
}
