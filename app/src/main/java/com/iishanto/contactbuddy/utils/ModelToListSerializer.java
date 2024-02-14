package com.iishanto.contactbuddy.utils;

import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.iishanto.contactbuddy.model.ArrayModelConvertible;
import com.iishanto.contactbuddy.model.Model;

import java.io.IOException;
import java.io.Serializable;

public class ModelToListSerializer extends JsonSerializer<ArrayModelConvertible> {
    private String TAG="MODEL_TO_LIST_SERIALIZER";

    @Override
    public void serialize(ArrayModelConvertible value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Log.i(TAG, "serialize: JOB STARTED");
        gen.writeStartArray();
        for (Model val:value.getData()){
            gen.writeTree(val.toJsonNode());
        }
        gen.writeEndArray();
    }
}
