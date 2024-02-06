package com.iishanto.contactbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginSuccessResponse {
    private String status;
    private String token;
    private String message;
    private User data;

    public void setData(User data) {
        this.data = data;
    }

    public User getData() {
        return data;
    }

    private Boolean skipLogin;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginSuccessResponse(){}
    public LoginSuccessResponse(String status, String token, User user) {
        this.status = status;
        this.token = token;
        this.data=user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
