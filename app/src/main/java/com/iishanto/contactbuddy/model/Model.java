package com.iishanto.contactbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Model implements Serializable {
    public JsonNode toJsonNode(){
        return new ObjectMapper().valueToTree(this);
    }
}
