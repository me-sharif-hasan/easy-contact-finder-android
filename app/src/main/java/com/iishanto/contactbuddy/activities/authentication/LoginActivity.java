package com.iishanto.contactbuddy.activities.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.service.socialLoginService.GoogleAuthenticationService;

public class LoginActivity extends AppCompatActivity {
    protected static final String TAG="LOGIN_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG, "onCreate: Authenticating using google");
        GoogleAuthenticationService googleAuthenticationService=new GoogleAuthenticationService(this);
        googleAuthenticationService.google(null);
    }
}