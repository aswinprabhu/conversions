package com.wavefront.labs.convert.converter.datadog.models;


import com.fasterxml.jackson.annotation.JsonInclude;


public class DatadogAlertOptionThresholds {
    private String critical;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String warning;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String ok;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String critical_recovery;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String warning_recovery;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String unknown;

    public String getCritical() { return critical; }

    public void setCritical(String critical) { this.critical = critical; }

    public String getWarning() { return warning; }

    public void setWarning(String warning) { this.warning = warning; }

    public String getOk() { return ok; }

    public void setOk(String ok) { this.ok = ok; }

    public String getCritical_recovery() { return critical_recovery; }

    public void setCritical_recovery(String critical_recovery) { this.critical_recovery = critical_recovery; }

    public String getWarning_recovery() { return warning_recovery; }

    public void setWarning_recovery(String warning_recovery) { this.warning_recovery = warning_recovery; }

    public String getUnknown() { return unknown; }

    public void setUnknown(String unknown) { this.unknown = unknown; }
}
