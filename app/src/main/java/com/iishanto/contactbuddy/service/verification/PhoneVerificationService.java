package com.iishanto.contactbuddy.service.verification;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.PhoneVerificationModel;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

public class PhoneVerificationService {
    private static String TAG="PHONE_VERIFICATION_SERVICE";
    private String phoneNumber;
    private HttpClient httpClient;
    PhoneVerificationModel phoneVerificationModel;
    Context context;
    public PhoneVerificationService(String phoneNumber, Context context){
        this.context=context;
        this.phoneNumber=phoneNumber;
        httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,context);
    }
    public void sendVerificationCode(HttpEvent httpEvent){
        phoneVerificationModel=new PhoneVerificationModel();
        phoneVerificationModel.setPhone(phoneNumber);
        httpClient.post("/api/verification/send-verification-code", phoneVerificationModel, httpEvent);
    }

    public void verify(String code,HttpEvent httpEvent){
        phoneVerificationModel.setCode(code);
        httpClient.post("/api/verification/verify",phoneVerificationModel,httpEvent);
    }
}
