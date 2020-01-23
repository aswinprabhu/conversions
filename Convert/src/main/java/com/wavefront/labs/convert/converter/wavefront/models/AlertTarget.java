package com.wavefront.labs.convert.converter.wavefront.models;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.wavefront.rest.models.TargetInfo.MethodEnum;


public class AlertTarget extends com.wavefront.rest.models.TargetInfo {
    /**
     * Notification method of the alert target
     */
    @SerializedName("method")
    private MethodEnum method = null;

    @SerializedName("id")
    private String id = null;

    @SerializedName("name")
    private String name = null;

    /**
     * Notification method of the alert target
     * @return method
     **/
    public MethodEnum getMethod() {
        return method;
    }

    public void setMethod(MethodEnum methodEnum) { method = methodEnum; }

    /**
     * ID of the alert target
     * @return id
     **/
    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    /**
     * Name of the alert target
     * @return name
     **/
    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlertTarget alertTarget = (AlertTarget) o;
        return Objects.equals(this.method, alertTarget.method) &&
                Objects.equals(this.id, alertTarget.id) &&
                Objects.equals(this.name, alertTarget.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, id, name);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AlertTarget {\n");

        sb.append("    method: ").append(toIndentedString(method)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
