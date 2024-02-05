package com.iishanto.contactbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Model {
    private String uuid;
    private String name;
    private String email;
    private String picture;
    private Phones[] phones;
    private Boolean useSocialLogin;
    private String country;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Phones[] getPhones() {
        return phones;
    }

    public void setPhones(Phones[] phones) {
        this.phones = phones;
    }

    public Boolean getUseSocialLogin() {
        return useSocialLogin;
    }

    public void setUseSocialLogin(Boolean useSocialLogin) {
        this.useSocialLogin = useSocialLogin;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
