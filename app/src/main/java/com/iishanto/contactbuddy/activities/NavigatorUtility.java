package com.iishanto.contactbuddy.activities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.iishanto.contactbuddy.activities.authentication.LoginActivity;
import com.iishanto.contactbuddy.activities.home.HomePageActivity;
import com.iishanto.contactbuddy.activities.registration.UserRegistrationActivity;
import com.iishanto.contactbuddy.activities.setup.UserAccountSetupActivity;

public class NavigatorUtility {
    private NavigatorUtility(){};
    private static NavigatorUtility instance;
    private static AppCompatActivity appCompatActivity;

    public void SwitchToHomePage(){
        Intent i=new Intent(appCompatActivity, HomePageActivity.class);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }

    public void SwitchToSetupPage(){
        Intent i=new Intent(appCompatActivity, UserAccountSetupActivity.class);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }

    public void SwitchToLoginPage(){
        Intent i=new Intent(appCompatActivity, LoginActivity.class);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }

    public static NavigatorUtility getInstance(AppCompatActivity appCompatActivity) {
        NavigatorUtility.appCompatActivity=appCompatActivity;
        if(instance==null) instance=new NavigatorUtility();
        return instance;
    }

    public void SwitchRegistrationPage() {
        Intent i=new Intent(appCompatActivity, UserRegistrationActivity.class);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }
}
