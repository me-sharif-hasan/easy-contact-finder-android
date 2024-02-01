package com.iishanto.contactbuddy.events;

import com.iishanto.contactbuddy.model.User;

public abstract class UserAuthEvents {
    public void onSuccess(User user){}
    public void onError(Exception e){}
    public void onLoading(){}
}
