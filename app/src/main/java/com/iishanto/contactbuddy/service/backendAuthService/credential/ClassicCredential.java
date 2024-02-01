package com.iishanto.contactbuddy.service.backendAuthService.credential;

import com.iishanto.contactbuddy.ConfigurationSingleton;

public class ClassicCredential implements BackendCredential{
    @Override
    public String getType() {
        return ConfigurationSingleton.classicAuthType;
    }
}
