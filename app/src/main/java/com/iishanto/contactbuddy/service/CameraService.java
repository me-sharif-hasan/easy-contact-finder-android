package com.iishanto.contactbuddy.service;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

public class CameraService {
    AppCompatActivity context;
    public CameraService(AppCompatActivity context){
        this.context=context;
    }

    public void startCameraX(ProcessCameraProvider processCameraProvider, PreviewView previewView, ImageCapture imageCapture){
        startCameraX(processCameraProvider,previewView,imageCapture,false);
    }
    public void startCameraX(ProcessCameraProvider processCameraProvider, PreviewView previewView, ImageCapture imageCapture,Boolean backCamera) {
        processCameraProvider.unbindAll();
        CameraSelector cameraSelector=new CameraSelector.Builder().requireLensFacing(backCamera?CameraSelector.LENS_FACING_BACK:CameraSelector.LENS_FACING_FRONT).build();
        Preview preview=new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();
        processCameraProvider.bindToLifecycle(context,cameraSelector,preview,imageCapture);
    }

}
