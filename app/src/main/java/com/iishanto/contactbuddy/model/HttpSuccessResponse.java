package com.iishanto.contactbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpSuccessResponse {
    private String status;
    private String message;
    private JsonNode data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
