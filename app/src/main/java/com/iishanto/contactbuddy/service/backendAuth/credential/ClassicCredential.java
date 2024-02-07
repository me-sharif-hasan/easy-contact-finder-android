package com.iishanto.contactbuddy.service.backendAuth.credential;

import com.iishanto.contactbuddy.UtilityAndConstantsProvider;


public class ClassicCredential extends BackendCredential{
    private String email;
    private String password;

    public ClassicCredential(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getType() {
        return UtilityAndConstantsProvider.classicAuthType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
