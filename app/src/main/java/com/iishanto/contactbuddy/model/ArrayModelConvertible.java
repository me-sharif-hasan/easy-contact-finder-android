package com.iishanto.contactbuddy.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iishanto.contactbuddy.utils.ModelToListSerializer;

import java.io.Serializable;

@JsonSerialize(using = ModelToListSerializer.class)
public interface ArrayModelConvertible {
    Iterable <? extends Model> getData();
}
