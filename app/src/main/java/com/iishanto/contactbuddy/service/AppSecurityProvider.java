package com.iishanto.contactbuddy.service;

public class AppSecurityProvider {
    private AppSecurityProvider(){}
    private static AppSecurityProvider instance;

    private String securityToken=null;

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public static AppSecurityProvider getInstance() {
        if(instance==null) instance=new AppSecurityProvider();
        return instance;
    }
}
