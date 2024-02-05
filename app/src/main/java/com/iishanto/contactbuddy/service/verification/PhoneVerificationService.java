package com.iishanto.contactbuddy.service.verification;

import android.util.Log;

import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.PhoneVerificationModel;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

public class PhoneVerificationService {
    private static String TAG="PHONE_VERIFICATION_SERVICE";
    private String phoneNumber;
    private HttpClient httpClient;
    public PhoneVerificationService(String phoneNumber){
        this.phoneNumber=phoneNumber;
        httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl);
    }
    public void sendVerificationCode(){
        PhoneVerificationModel phoneVerificationModel=new PhoneVerificationModel();
        phoneVerificationModel.setPhone(phoneNumber);
        httpClient.post("/api/phone/send-verification-code", phoneVerificationModel, new HttpEvent() {
            @Override
            public void success(String data) {
                Log.i(TAG, "success: "+data);
                super.success(data);
            }

            @Override
            public void failure(Exception e) {
                e.printStackTrace();
                super.failure(e);
            }
        });
    }

    public Boolean verify(){
        return false;
    }
}
