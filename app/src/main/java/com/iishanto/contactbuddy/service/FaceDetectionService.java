package com.iishanto.contactbuddy.service;

import android.graphics.Bitmap;

import com.iishanto.contactbuddy.events.OnFaceDetected;

public class FaceDetectionService {
    public void detect(Bitmap bitmap, OnFaceDetected onFaceDetectedEvent){
        onFaceDetectedEvent.onFaceDetected(bitmap);
    }
}
