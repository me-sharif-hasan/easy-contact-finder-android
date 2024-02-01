package com.iishanto.contactbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Model {
    private String uuid;
    private String fullName;
    private String email;
    private String photo;

    private String[] mobileNumbers;
}
