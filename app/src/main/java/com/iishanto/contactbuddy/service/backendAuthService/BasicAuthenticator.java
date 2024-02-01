package com.iishanto.contactbuddy.service.backendAuthService;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.iishanto.contactbuddy.ConfigurationSingleton;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.events.UserAuthEvents;
import com.iishanto.contactbuddy.service.backendAuthService.credential.BackendCredential;
import com.iishanto.contactbuddy.service.backendAuthService.credential.ClassicCredential;
import com.iishanto.contactbuddy.service.backendAuthService.credential.GoogleAuthCredential;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

public class BasicAuthenticator implements Authenticate{
    private static final String TAG="AUTHENTICATE";
    @Override
    public void auth(BackendCredential credential, UserAuthEvents userAuthEvents) {
        if(credential.getType().equals(ConfigurationSingleton.googleAuthType)){
            googleAuth((GoogleAuthCredential) credential,userAuthEvents);
        }else if(credential.getType().equals(ConfigurationSingleton.classicAuthType)){
            classicAuth((ClassicCredential) credential,userAuthEvents);
        }
    }

    private void googleAuth(GoogleAuthCredential googleAuthCredential,UserAuthEvents userAuthEvents){
        HttpClient httpClient=new OkHttpClientImpl(ConfigurationSingleton.baseUrl);
        httpClient.post("/auth/login", googleAuthCredential, new HttpEvent() {
            @Override
            public void success(String data) {
                Log.i(TAG, "success: "+data);
//                userAuthEvents.onSuccess(data);
            }

            @Override
            public void failure(Exception e) {
                userAuthEvents.onError(e);
            }
        });
    }
    private void classicAuth(ClassicCredential classicCredential,UserAuthEvents userAuthEvents){

    }
}
