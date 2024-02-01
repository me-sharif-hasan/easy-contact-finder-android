package com.iishanto.contactbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.iishanto.contactbuddy.activities.authentication.LoginActivity;

import java.util.concurrent.TimeUnit;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    AppCompatActivity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context=this;
        //try auto login

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Intent i=new Intent(context, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }).start();
    }
}