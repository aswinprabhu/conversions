package com.wavefront.labs.convert.converter.datadog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties({"fill"})
@JsonDeserialize(using=DatadogGraphRequests.DatadogGraphRequestsDeserializer.class)
public class DatadogGraphRequests {

    private boolean containsRequestArray = true;

    private List<DatadogGraphRequest> requests = new ArrayList<DatadogGraphRequest>();
    private DatadogGraphRequest request = new DatadogGraphRequest();

    public DatadogGraphRequest get(int index) {
        DatadogGraphRequest req = null;
        if (containsRequestArray) {
            req = requests.get(index);
        } else if (index == 0) {
            req = request;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
        return req;
    }

    public List<DatadogGraphRequest> getRequests() {
        if (containsRequestArray) {
            return requests;
        } else {
            return Arrays.asList(new DatadogGraphRequest[]{request});
        }
    }

    public int size() {
        return containsRequestArray ? requests.size() : 1;
    }

    public static class DatadogGraphRequestsDeserializer extends StdDeserializer<DatadogGraphRequests> {
        public DatadogGraphRequestsDeserializer() { this(null); }
        public DatadogGraphRequestsDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public DatadogGraphRequests deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            DatadogGraphRequests ddGraphReqs = new DatadogGraphRequests();

            ObjectMapper objMapper = new ObjectMapper();
            JsonNode node = jp.getCodec().readTree(jp);
            if (node.isArray()) {
                ddGraphReqs.containsRequestArray = true;
                for (final JsonNode obj : node) {
                    ddGraphReqs.requests.add(objMapper.convertValue(obj, DatadogGraphRequest.class));
                }
            } else {
                ddGraphReqs.containsRequestArray = false;
                ddGraphReqs.request = objMapper.convertValue(node.get("fill"), DatadogGraphRequest.class);
            }

            return ddGraphReqs;
        }
    }
}
