package com.iishanto.contactbuddy.activities.setup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.github.leandroborgesferreira.loadingbutton.customViews.OnAnimationEndListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.internal.FaceDetectorImpl;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.InitialSetupModel;
import com.iishanto.contactbuddy.permissionManagement.CameraPermissionTaker;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;
import com.iishanto.contactbuddy.service.verification.PhoneVerificationService;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.Executor;

public class UserAccountSetupActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG="USER_ACCOUNT_SETUP_ACTIVITY";
    private PreviewView previewView;
    private ImageCapture imageCapture;
    ListenableFuture<ProcessCameraProvider> processCameraProviderListenableFuture;
    private TextView noFaceFoundError;

    InitialSetupModel initialSetupModel;

    CircularProgressButton circularProgressButton;

    LinearLayout phoneVerificationFields;
    EditText phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_setup);
        HttpClient httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl);
        previewView=findViewById(R.id.setup_camera_preview);
        circularProgressButton=findViewById(R.id.setup_take_picture_button);
        noFaceFoundError=findViewById(R.id.setup_no_face_found_error);
        phoneVerificationFields=findViewById(R.id.phone_verification_fields);
        phoneNumber=findViewById(R.id.setup_phone_number_field);
        initialSetupModel=new InitialSetupModel();
        circularProgressButton.setOnClickListener(this);
        new CameraPermissionTaker(this).askForCamera();
        processCameraProviderListenableFuture=ProcessCameraProvider.getInstance(this);
        processCameraProviderListenableFuture.addListener(()->{
            try{
                ProcessCameraProvider processCameraProvider=processCameraProviderListenableFuture.get();
                startCameraX(processCameraProvider);
            }catch (Throwable e){
                Log.i(TAG, "onCreate: "+e.getLocalizedMessage());
            }
        }, getExecutor());
        httpClient.get("/api", new HttpEvent() {
            @Override
            public void success(String data) {
                System.out.println(data);
            }

            @Override
            public void failure(Exception e) {
                 e.printStackTrace();
            }
        });
    }

    private Executor getExecutor(){
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX(ProcessCameraProvider processCameraProvider) {
        processCameraProvider.unbindAll();
        CameraSelector cameraSelector=new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        Preview preview=new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();
        processCameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture);
    }


    @Override
    public void onClick(View v) {
        if(v==circularProgressButton){
            Log.i(TAG, "onClick: Capturing");
            circularProgressButton.startAnimation();
            imageCapture.takePicture(getExecutor(), new ImageCapture.OnImageCapturedCallback() {
                @Override
                public void onCaptureSuccess(@NonNull ImageProxy image) {
                    Log.i(TAG, "onCaptureSuccess: Capture success, "+image.getWidth()+"; "+image.getHeight());
                    Bitmap bitmap=UtilityAndConstantsProvider.convertImageProxyToBitmap(image);

                    FaceDetectorOptions options =
                            new FaceDetectorOptions.Builder()
                                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                                    .setMinFaceSize(0.15f)
                                    .build();
                    FaceDetector faceDetector= FaceDetection.getClient(options);
                    InputImage inputImage=InputImage.fromBitmap(UtilityAndConstantsProvider.convertImageProxyToBitmap(image),0);
                    image.close();
                    faceDetector.process(inputImage)
                            .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                                @Override
                                public void onSuccess(List<Face> faces) {
                                    circularProgressButton.revertAnimation();
                                    if(faces.size()==0){
                                        noFaceFoundError.setText("Have some light and come bit more closer");
                                        noFaceFoundError.setVisibility(View.VISIBLE);
                                    }else{
                                        noFaceFoundError.setVisibility(View.INVISIBLE);
                                        Rect boundingBox=faces.get(0).getBoundingBox();
                                        int x = boundingBox.left;
                                        int y = boundingBox.top;
                                        int width = boundingBox.width();
                                        int height = boundingBox.height();
                                        Log.i(TAG, "onSuccess: "+boundingBox);
                                        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);

                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                                        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        initialSetupModel.setBase64Image(base64Image);
                                        takePhoneInput();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    circularProgressButton.revertAnimation();
                                    noFaceFoundError.setText("Face detection failed");
                                    noFaceFoundError.setVisibility(View.VISIBLE);
                                }
                            });
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    super.onError(exception);
                    circularProgressButton.revertAnimation();
                }
            });
        }
    }

    private void takePhoneInput() {
        phoneVerificationFields.setVisibility(View.VISIBLE);
        circularProgressButton.stopProgressAnimation();
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=phoneNumber.getText().toString();
                PhoneVerificationService phoneVerificationService=new PhoneVerificationService(phone);
                phoneVerificationService.sendVerificationCode();
            }
        });
    }


}