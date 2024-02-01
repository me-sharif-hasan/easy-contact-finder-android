package com.iishanto.contactbuddy.service.backendAuthService.credential;

import com.iishanto.contactbuddy.ConfigurationSingleton;
import com.iishanto.contactbuddy.model.Model;

public class GoogleAuthCredential extends BackendCredential{
    String email;
    String token;
    @Override
    public String getType() {
        return ConfigurationSingleton.googleAuthType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
