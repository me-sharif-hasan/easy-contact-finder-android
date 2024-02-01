package com.iishanto.contactbuddy.service.http;

import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.Model;

public abstract class HttpClient {
    public void get(String url){};
    public void put(String url){};
    public void delete(String url){};

    public abstract void get(String url, HttpEvent httpEvent);

    public abstract void post(String url, Model model, HttpEvent httpEvent);
}
