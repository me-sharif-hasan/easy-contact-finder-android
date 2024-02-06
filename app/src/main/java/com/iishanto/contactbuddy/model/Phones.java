package com.iishanto.contactbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Phones {
    private String id;
    private String number;
    private String country;
    private String countryCode;
    private PhoneVerificationModel phoneVerification;

    public PhoneVerificationModel getPhoneVerification() {
        return phoneVerification;
    }

    public void setPhoneVerification(PhoneVerificationModel phoneVerificationModel) {
        this.phoneVerification = phoneVerificationModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
