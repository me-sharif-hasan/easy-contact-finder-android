package com.iishanto.contactbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.contactbuddy.activities.NavigatorUtility;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.LoginSuccessResponse;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.permissionManagement.PermissionManager;
import com.iishanto.contactbuddy.service.AppSecurityProvider;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

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
//                User user=new ObjectMapper();
                try{
                    Log.i(TAG, "success: "+data);
                    LoginSuccessResponse loginSuccessResponse=new ObjectMapper().readValue(data,LoginSuccessResponse.class);
                    AppSecurityProvider.getInstance().setUser(loginSuccessResponse.getData());
                    if(AppSecurityProvider.getInstance().getUser()==null) throw new Exception("Invalid user");
                    Log.i(TAG, "success: "+AppSecurityProvider.getInstance().getUser().getIsPhotoVerified());
                    if (AppSecurityProvider.getInstance().getUser().getIsPhotoVerified())
                        NavigatorUtility.getInstance(context).switchToHomePage();
                    else
                        NavigatorUtility.getInstance(context).switchToSetupPage();
                }catch (Exception e){
                    failure(e);
                }
            }

            @Override
            public void failure(Exception e) {
                NavigatorUtility.getInstance(context).switchToLoginPage();
            }
        });
    }
}