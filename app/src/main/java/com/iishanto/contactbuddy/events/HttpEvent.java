package com.iishanto.contactbuddy.events;

public abstract class HttpEvent{
    public void success(String data){}
    public void failure(Exception e){}
}
