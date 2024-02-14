package com.iishanto.contactbuddy.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.iishanto.contactbuddy.model.User;

public class AppSecurityProvider {
    private static String TAG="APP_SECURITY_PROVIDER";
    private AppSecurityProvider(){}
    private static AppSecurityProvider instance;

    private String securityToken=null;
    private User user;

    public void setSecurityToken(String securityToken, Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("smart-contact-app", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("authorization",securityToken).apply();
        this.securityToken = securityToken;
    }

    public String getSecurityToken(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("smart-contact-app", Context.MODE_PRIVATE);
        Log.i(TAG, "getSecurityToken: "+sharedPreferences.getString("authorization","<no-auth>"));
        return sharedPreferences.getString("authorization","<no-auth>");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        Log.i(TAG, "setUser: setting the user."+user.getName());
        this.user = user;
    }

    public static AppSecurityProvider getInstance() {
        if(instance==null) instance=new AppSecurityProvider();
        return instance;
    }
}
