package com.iishanto.contactbuddy.service.userRegistration;

import android.content.Context;

import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.UserRegistrationModel;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

public class UserRegistrationService {
    HttpClient httpClient;
    public UserRegistrationService(Context context){
        httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,context);
    }
    public void register(UserRegistrationModel userRegistrationModel, HttpEvent httpEvent){
        httpClient.post("/auth/email-register",userRegistrationModel,httpEvent);
    }
}
