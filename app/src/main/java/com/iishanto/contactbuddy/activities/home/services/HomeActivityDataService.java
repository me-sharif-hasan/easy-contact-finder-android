package com.iishanto.contactbuddy.activities.home.services;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.events.UserLoadedEvent;
import com.iishanto.contactbuddy.model.HttpSuccessResponse;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HomeActivityDataService {
    Context context;
    HttpClient httpClient;
    Long TIME_LIMIT= 10000L;
    public HomeActivityDataService(Context context){
        this.context=context;
        httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,context);
    }

    public void getUserInfo(UserLoadedEvent userLoadedEvent){
        httpClient.get("/api/user", new HttpEvent() {
            @Override
            public void success(String data) {
                try{
                    HttpSuccessResponse httpSuccessResponse=new ObjectMapper().readValue(data,HttpSuccessResponse.class);
                    if(httpSuccessResponse.getStatus().equals("error")) throw new UserNotAuthenticatedException("Error when getting current user");
                    User user=new ObjectMapper().convertValue(httpSuccessResponse.getData(),User.class);
                    if(context instanceof AppCompatActivity){
                        ((AppCompatActivity) context).runOnUiThread(()->{userLoadedEvent.success(user);});
                    }else {
                        userLoadedEvent.success(user);
                    }
                }catch (Exception e){
                    if(context instanceof AppCompatActivity){
                        ((AppCompatActivity) context).runOnUiThread(()->userLoadedEvent.failure(e));
                    }else userLoadedEvent.failure(e);
                }
            }

            @Override
            public void failure(Exception e) {
                userLoadedEvent.failure(e);
            }
        });
    }

}
