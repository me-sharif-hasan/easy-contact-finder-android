package com.iishanto.contactbuddy.service.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.iishanto.contactbuddy.events.ImageLoadedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageService {
    Context context;
    public ImageService(Context context){
        this.context=context;
    }

    public void urlToBitmap(String url, ImageLoadedEvent imageLoadedEvent){
        new Thread(() -> {
            try{
                Bitmap b=getBitmapFromURL(url);
                if(b==null) throw new Exception("Empty image");
                if(context instanceof AppCompatActivity){
                    ((AppCompatActivity) context).runOnUiThread(()->imageLoadedEvent.success(b));
                }else imageLoadedEvent.success(b);
            }catch (Exception e){
                if(context instanceof AppCompatActivity) ((AppCompatActivity) context).runOnUiThread(()->imageLoadedEvent.failure(e));
                else imageLoadedEvent.failure(e);
            }
        }).start();
    }

    public static Bitmap getBitmapFromURL(String src) throws Exception {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            throw e;
        }
    }
}
