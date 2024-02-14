package com.iishanto.contactbuddy.model;

import java.util.List;
public class SaveContactCollections extends Model implements ArrayModelConvertible {
    private List <SaveContactModel> data;

    public void setData(List<SaveContactModel> data) {
        this.data = data;
    }

    @Override
    public List<SaveContactModel> getData() {
        return data;
    }
}
