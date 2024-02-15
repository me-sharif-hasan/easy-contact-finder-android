package com.iishanto.contactbuddy.service.user.backendAuth;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.events.UserAuthEvents;
import com.iishanto.contactbuddy.model.LoginSuccessResponse;
import com.iishanto.contactbuddy.service.AppSecurityProvider;
import com.iishanto.contactbuddy.service.user.backendAuth.credential.BackendCredential;
import com.iishanto.contactbuddy.service.user.backendAuth.credential.ClassicCredential;
import com.iishanto.contactbuddy.service.user.backendAuth.credential.GoogleAuthCredential;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

public class BasicAuthenticator implements Authenticate{
    private static final String TAG="BASIC_AUTHENTICATE";
    Context context;
    public BasicAuthenticator(Context context){
        this.context=context;
    }
    @Override
    public void auth(BackendCredential credential, UserAuthEvents userAuthEvents) {
        if(credential.getType().equals(UtilityAndConstantsProvider.googleAuthType)){
            googleAuth((GoogleAuthCredential) credential,userAuthEvents);
        }else if(credential.getType().equals(UtilityAndConstantsProvider.classicAuthType)){
            classicAuth((ClassicCredential) credential,userAuthEvents);
        }
    }

    private void googleAuth(GoogleAuthCredential googleAuthCredential, UserAuthEvents userAuthEvents){
        HttpClient httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,context);
        Log.i(TAG, "googleAuth: "+googleAuthCredential.getToken());
        httpClient.post("/auth/with-google", googleAuthCredential, new HttpEvent() {
            @Override
            public void success(String data) {
                try{
                    Log.i(TAG, "success: user data: "+data);
                    LoginSuccessResponse loginSuccessResponse= new ObjectMapper().readValue(data, LoginSuccessResponse.class);
                    AppSecurityProvider.getInstance().setSecurityToken(loginSuccessResponse.getToken(),context);
                    userAuthEvents.onSuccess(loginSuccessResponse.getData());
                }catch (Exception e){
                    failure(e);
                }
            }

            @Override
            public void failure(Exception e) {
                userAuthEvents.onError(e);
            }
        });
    }
    private void classicAuth(ClassicCredential classicCredential, UserAuthEvents userAuthEvents){
        HttpClient httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,context);
        Log.i(TAG, "classicAuth: "+classicCredential+" "+classicCredential.getEmail()+" "+classicCredential.getPassword());
        httpClient.post("/auth/email-login", classicCredential, new HttpEvent() {
            @Override
            public void success(String data) {
                try{
                    LoginSuccessResponse loginSuccessResponse= new ObjectMapper().readValue(data, LoginSuccessResponse.class);
                    AppSecurityProvider.getInstance().setSecurityToken(loginSuccessResponse.getToken(),context);
                    userAuthEvents.onSuccess(loginSuccessResponse.getData());
                }catch (Exception e){
                    failure(e);
                }
            }

            @Override
            public void failure(Exception e) {
                Log.i(TAG, "failure: Failure login");
                userAuthEvents.onError(e);
            }
        });
    }
}
