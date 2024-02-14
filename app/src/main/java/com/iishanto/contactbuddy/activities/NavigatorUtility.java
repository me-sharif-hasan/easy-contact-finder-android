package com.iishanto.contactbuddy.activities;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.iishanto.contactbuddy.activities.authentication.LoginActivity;
import com.iishanto.contactbuddy.activities.camera.CameraActivity;
import com.iishanto.contactbuddy.activities.contactManagement.SaveContactActivity;
import com.iishanto.contactbuddy.activities.home.HomePageActivity;
import com.iishanto.contactbuddy.activities.registration.UserRegistrationActivity;
import com.iishanto.contactbuddy.activities.setup.UserAccountSetupActivity;
import com.iishanto.contactbuddy.model.Phones;
import com.iishanto.contactbuddy.model.User;

public class NavigatorUtility {
    private NavigatorUtility(){};
    private static NavigatorUtility instance;
    private static AppCompatActivity appCompatActivity;

    public void switchToHomePage(){
        Intent i=new Intent(appCompatActivity, HomePageActivity.class);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }

    public void switchToSetupPage(){
        Intent i=new Intent(appCompatActivity, UserAccountSetupActivity.class);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }

    public void switchToLoginPage(String message){
        Intent i=new Intent(appCompatActivity, LoginActivity.class);
        i.putExtra("message",message);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }

    public void switchToContactFinderWithScanPage(){
        Intent i=new Intent(appCompatActivity, CameraActivity.class);
        appCompatActivity.startActivity(i);
    }

    public void switchToLoginPage(){
        switchToLoginPage("");
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

    public void switchToNumberSavingPage(User user) {
        Intent i=new Intent(appCompatActivity, SaveContactActivity.class);
        i.putExtra("user",user);
        appCompatActivity.startActivity(i);
        appCompatActivity.finish();
    }
}
