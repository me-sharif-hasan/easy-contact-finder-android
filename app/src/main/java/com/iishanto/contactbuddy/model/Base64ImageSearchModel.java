package com.iishanto.contactbuddy.model;

import androidx.transition.Visibility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Base64ImageSearchModel extends Model {
    private String base_64_encoded_string;
    private User user;

    public String getBase_64_encoded_string() {
        return base_64_encoded_string;
    }

    public void setBase_64_encoded_string(String base_64_encoded_string) {
        this.base_64_encoded_string = base_64_encoded_string;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
