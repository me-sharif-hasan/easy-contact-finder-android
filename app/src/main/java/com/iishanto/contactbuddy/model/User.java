package com.iishanto.contactbuddy.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Model {
    private Long id;
    private String uuid;
    private String name;
    private String email;
    private String picture;
    private Phones[] phones;
    private Boolean useSocialLogin;
    private String country;
    private Boolean isPhotoVerified;
    private Float score;

    public Boolean getIsPhotoVerified() {
        return isPhotoVerified;
    }

    public void setIsPhotoVerified(Boolean photoVerified) {
        isPhotoVerified = photoVerified;
    }

    private UserVerificationModel userVerification;

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

    public UserVerificationModel getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(UserVerificationModel userVerification) {
        this.userVerification = userVerification;
    }

    public Boolean getPhotoVerified() {
        return isPhotoVerified;
    }

    public void setPhotoVerified(Boolean photoVerified) {
        isPhotoVerified = photoVerified;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
