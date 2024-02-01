package com.iishanto.contactbuddy.service.http;

import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.Model;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientImpl extends HttpClient{
    okhttp3.OkHttpClient okHttpClient;
    public OkHttpClientImpl(){
        this.okHttpClient= new OkHttpClient.Builder().build();
    }
    @Override
    public void get(String url, HttpEvent httpEvent) {
        okHttpClient.newCall(getOkHttpGetRequest(url)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpEvent.failure(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data=response.body().string();
                httpEvent.success(data);
            }
        });
    }

    @Override
    public void post(String url,Model model,HttpEvent httpEvent) {
        okHttpClient.newCall(getOkHttpPostRequest(url,model)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpEvent.failure(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
