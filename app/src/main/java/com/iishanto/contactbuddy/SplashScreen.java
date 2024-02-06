package com.iishanto.contactbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.iishanto.contactbuddy.activities.NavigatorUtility;
import com.iishanto.contactbuddy.activities.authentication.LoginActivity;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

import java.util.concurrent.TimeUnit;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    private static final String TAG="SPLASH_SCREEN";
    AppCompatActivity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context=this;
        HttpClient httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,this);
        httpClient.get("api", new HttpEvent() {
            @Override
            public void success(String data) {
                NavigatorUtility.getInstance(context).SwitchToHomePage();
            }

            @Override
            public void failure(Exception e) {
                NavigatorUtility.getInstance(context).SwitchToLoginPage();
            }
        });
    }
}