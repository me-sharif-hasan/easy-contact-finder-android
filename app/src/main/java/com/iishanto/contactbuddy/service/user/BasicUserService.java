package com.iishanto.contactbuddy.service.user;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.events.UserSearchEvent;
import com.iishanto.contactbuddy.model.Base64ImageSearchModel;
import com.iishanto.contactbuddy.model.HttpSuccessResponse;
import com.iishanto.contactbuddy.model.Phones;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BasicUserService {
    private static String TAG="BASIC_USER_SERVICE";
    private Context context;
    HttpClient httpClient;
    public BasicUserService(Context context){
        this.context=context;
        httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,context);
    }

    public void searchUserByImage(Base64ImageSearchModel imageSearchModel, UserSearchEvent userSearchEvent){
        Log.i(TAG, "searchUserByImage: Searching user");
//        List <User> dummy=new ArrayList<>();
//        User user1=new User();
//        user1.setId(1L);
//        user1.setName("Lamyea Sultana");
//        user1.setEmail("lam@gmail.com");
//        user1.setPicture("https://lh3.googleusercontent.com/a/ACg8ocLcbHTXAMEGfu-HUxDE-fHgrhvEsHTbbl4u_bnNeF-N4zE=s96-c");
//        Phones phones=new Phones();
//        phones.setNumber("01638757250");
//        user1.setPhones((new Phones[]{phones}));
//        dummy.add(user1);
//        if(context instanceof AppCompatActivity){
//            ((AppCompatActivity) context).runOnUiThread(() -> {
//                Log.i(TAG, "searchUserByImage: calling");
//                userSearchEvent.success(dummy);
//            });
//        }
        httpClient.post("/api/user/search-by-image", imageSearchModel, new HttpEvent() {
            @Override
            public void success(String data) {
                try{
                    HttpSuccessResponse httpSuccessResponse=new ObjectMapper().readValue(data,HttpSuccessResponse.class);
                    JsonNode users =httpSuccessResponse.getData();
                    ObjectReader objectReader=new ObjectMapper().readerFor(new TypeReference<List<User>>() {});
                    List < User> userList=objectReader.readValue(users);
                    if(context instanceof AppCompatActivity) ((AppCompatActivity) context).runOnUiThread(()-> userSearchEvent.success(userList));
                    else userSearchEvent.success(userList);
                }catch (Exception e){
                    failure(e);
                }
            }

            @Override
            public void failure(Exception e) {
                if(context instanceof AppCompatActivity) ((AppCompatActivity) context).runOnUiThread(()-> userSearchEvent.failure(e));
                else userSearchEvent.failure(e);
            }
        });
    }
}
