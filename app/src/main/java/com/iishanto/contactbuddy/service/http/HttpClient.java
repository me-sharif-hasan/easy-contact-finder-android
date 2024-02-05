package com.iishanto.contactbuddy.service.http;

import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.Model;
import com.iishanto.contactbuddy.service.AppSecurityProvider;

public abstract class HttpClient {
    public void get(String url){};
    public void put(String url){};
    public void delete(String url){};

    public abstract void get(String url, HttpEvent httpEvent);

    public abstract void post(String url, Model model, HttpEvent httpEvent);
    public static String getAuthorizationHeader() throws Exception{
        String token= AppSecurityProvider.getInstance().getSecurityToken();
        if(token==null) throw new Exception("User login token not found!");
        return "Bearer "+token;
    }
}
