package com.iishanto.contactbuddy.service;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import java.time.Duration;

public class CameraService {
    AppCompatActivity context;
    ProcessCameraProvider processCameraProvider;
    public CameraService(AppCompatActivity context,ProcessCameraProvider processCameraProvider){
        this.processCameraProvider=processCameraProvider;
        this.context=context;
    }

    public void startCameraX(PreviewView previewView, ImageCapture imageCapture) throws Exception{
        startCameraX(previewView,imageCapture,false);
    }
    public void startCameraX(PreviewView previewView, ImageCapture imageCapture,Boolean backCamera) throws Exception {
        processCameraProvider.unbindAll();
        CameraSelector cameraSelector=new CameraSelector.Builder().requireLensFacing(backCamera?CameraSelector.LENS_FACING_BACK:CameraSelector.LENS_FACING_FRONT).build();
        Preview preview=new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();
        processCameraProvider.bindToLifecycle(context,cameraSelector,preview,imageCapture);
    }

    public void unbind(){
        processCameraProvider.unbindAll();
    }

}
