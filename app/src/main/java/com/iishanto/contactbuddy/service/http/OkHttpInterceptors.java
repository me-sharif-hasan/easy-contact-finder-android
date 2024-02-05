package com.iishanto.contactbuddy.service.http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.iishanto.contactbuddy.service.AppSecurityProvider;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpInterceptors {
    public static class AuthorizationHeaderInserterInterceptor implements Interceptor {
        public static String TAG="AUTHORIZATION_HEADER_INTERCEPTOR";
        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request currentRequest=chain.request();
            try{
                Request newRequest=currentRequest.newBuilder().header("Authorization", HttpClient.getAuthorizationHeader()).build();
                return chain.proceed(newRequest);
            }catch (Exception e){
                Log.i(TAG, "intercept: user not logged in");
                return chain.proceed(currentRequest);
            }
        }
    }
}
