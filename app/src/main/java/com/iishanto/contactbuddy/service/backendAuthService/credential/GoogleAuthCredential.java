package com.iishanto.contactbuddy.service.backendAuthService.credential;

import com.iishanto.contactbuddy.ConfigurationSingleton;

public class GoogleAuthCredential implements BackendCredential{
    @Override
    public String getType() {
        return ConfigurationSingleton.googleAuthType;
    }
}
