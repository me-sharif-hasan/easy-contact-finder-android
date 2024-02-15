package com.iishanto.contactbuddy.service.user.backendAuth.credential;

import com.iishanto.contactbuddy.UtilityAndConstantsProvider;

public class GoogleAuthCredential extends BackendCredential{
    String email;
    String token;

    @Override
    public String getType() {
        return UtilityAndConstantsProvider.googleAuthType;
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
