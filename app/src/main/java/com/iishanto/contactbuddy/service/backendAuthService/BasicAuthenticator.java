package com.iishanto.contactbuddy.service.backendAuthService;

import com.iishanto.contactbuddy.ConfigurationSingleton;
import com.iishanto.contactbuddy.events.UserAuthEvents;
import com.iishanto.contactbuddy.service.backendAuthService.credential.BackendCredential;
import com.iishanto.contactbuddy.service.backendAuthService.credential.ClassicCredential;
import com.iishanto.contactbuddy.service.backendAuthService.credential.GoogleAuthCredential;

public class BasicAuthenticator implements Authenticate{
    @Override
    public void auth(BackendCredential credential, UserAuthEvents userAuthEvents) {
        if(credential.getType().equals(ConfigurationSingleton.googleAuthType)){
            googleAuth((GoogleAuthCredential) credential);
        }else if(credential.getType().equals(ConfigurationSingleton.classicAuthType)){
            classicAuth((ClassicCredential) credential);
        }
    }

    private void googleAuth(GoogleAuthCredential googleAuthCredential){

    }
    private void classicAuth(ClassicCredential classicCredential){

    }
}
