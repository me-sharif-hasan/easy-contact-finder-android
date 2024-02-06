package com.iishanto.contactbuddy.service.http;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.Model;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientImpl extends HttpClient{
    public static final String TAG="OK_HTTP_CLIENT_IMPL";
    okhttp3.OkHttpClient okHttpClient;
    private final HttpUrl baseUrl;
    public OkHttpClientImpl(AppCompatActivity context){
        super(context);
        baseUrl=null;
        init();
    }
    public OkHttpClientImpl(String baseUrl, Context context){
        super(context);
        this.baseUrl=HttpUrl.get(baseUrl);
        Log.i(TAG, "OkHttpClientImpl: "+this.baseUrl.toString());
        init();
    }
    private void init(){
        this.okHttpClient= new OkHttpClient.Builder().addInterceptor(new OkHttpInterceptors.AuthorizationHeaderInserterInterceptor()).build();
    }
    @Override
    public void get(String url, HttpEvent httpEvent) {
        HttpUrl newUrl=baseUrl.resolve(url);
        assert newUrl != null;
        okHttpClient.newCall(getOkHttpGetRequest(newUrl.toString())).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                httpEvent.failure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String data=response.body().string();
                if(!response.isSuccessful()) {
                    onFailure(call, new IOException("Server access denied"));
                    return;
                }
                httpEvent.success(data);
            }
        });
    }

    @Override
    public void post(String url,Model model,HttpEvent httpEvent) {
        HttpUrl newUrl=baseUrl.resolve(url);
        assert newUrl != null;
        okHttpClient.newCall(getOkHttpPostRequest(newUrl.toString(),model)).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                httpEvent.failure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i(TAG, "onResponse: Got response "+response.message());
                if(!response.isSuccessful()){
                    onFailure(call,new IOException("Server access denied"));
                    return;
                }
                assert response.body() != null;
                httpEvent.success(response.body().string());
            }
        });
    }

    private Request getOkHttpGetRequest(String url){
        return new Request.Builder().url(url).build();
    }
    private Request getOkHttpPostRequest(String url, Model model){
        return new Request.Builder().url(url).post(getRequestBody(model)).build();
    }

    private static RequestBody getRequestBody(Model object){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(JSON,object.toJsonNode().toString());
    }
}
