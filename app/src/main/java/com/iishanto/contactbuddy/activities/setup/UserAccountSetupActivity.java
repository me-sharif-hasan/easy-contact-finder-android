package com.iishanto.contactbuddy.activities.setup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.activities.NavigatorUtility;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.events.OnFaceDetected;
import com.iishanto.contactbuddy.model.InitialSetupModel;
import com.iishanto.contactbuddy.model.HttpSuccessResponse;
import com.iishanto.contactbuddy.permissionManagement.PermissionManager;
import com.iishanto.contactbuddy.service.CameraService;
import com.iishanto.contactbuddy.service.FaceDetectionService;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;
import com.iishanto.contactbuddy.service.verification.PhoneVerificationService;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.concurrent.Executor;

public class UserAccountSetupActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG="USER_ACCOUNT_SETUP_ACTIVITY";
    private PreviewView previewView;
    private ImageCapture imageCapture;
    ListenableFuture<ProcessCameraProvider> processCameraProviderListenableFuture;
    private TextView noFaceFoundError;
    private ImageView facePreview;

    InitialSetupModel initialSetupModel;

    CircularProgressButton circularProgressButton;

    LinearLayout phoneVerificationFields;
    EditText phoneNumber;
    EditText verificationCode;
    FaceDetectionService faceDetectionService;
    AppCompatActivity appCompatActivity;
    HttpClient httpClient;

    CameraService cameraService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_setup);
        appCompatActivity =this;
        cameraService=new CameraService(this);
        faceDetectionService=new FaceDetectionService();
        previewView=findViewById(R.id.setup_camera_preview);
        httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,this);
        circularProgressButton=findViewById(R.id.setup_take_picture_button);
        noFaceFoundError=findViewById(R.id.setup_no_face_found_error);
        phoneVerificationFields=findViewById(R.id.phone_verification_fields);
        phoneNumber=findViewById(R.id.setup_phone_number_field);
        facePreview=findViewById(R.id.setup_face_preview);
        initialSetupModel=new InitialSetupModel();
        circularProgressButton.setOnClickListener(this);
        verificationCode=findViewById(R.id.setup_verification_code);
        new PermissionManager(this).askForPermissions();

        processCameraProviderListenableFuture=ProcessCameraProvider.getInstance(this);
        processCameraProviderListenableFuture.addListener(()->{
            try{
                ProcessCameraProvider processCameraProvider=processCameraProviderListenableFuture.get();
                cameraService.startCameraX(processCameraProvider,previewView,imageCapture);
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




    @Override
    public void onClick(View v) {
        if(v==circularProgressButton){
            animateLoadingButton();
            Bitmap bitmap=previewView.getBitmap();
            faceDetectionService.detect(bitmap,new OnFaceDetected(){
                @Override
                public void onFaceDetected(Bitmap face) {
                    previewView.setVisibility(View.INVISIBLE);
                    facePreview.setVisibility(View.VISIBLE);
                    facePreview.setImageBitmap(face);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    if(bitmap==null){
                        onFaceNotDetected();
                        return;
                    }
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    initialSetupModel.setBase64Image(base64Image);
                    takePhoneInput();
                    circularProgressButton.revertAnimation();
                }

                @Override
                public void onFaceNotDetected() {
                    reset();
                    showError("Face detection failed");
                }
            });
        }
    }

    private void takePhoneInput() {
        phoneVerificationFields.setVisibility(View.VISIBLE);
        circularProgressButton.revertAnimation();
        circularProgressButton.setOnClickListener(v -> {
            String phone=phoneNumber.getText().toString();
            if(phone.length() == 0) return;
            PhoneVerificationService phoneVerificationService=new PhoneVerificationService(phone, getApplicationContext());
            animateLoadingButton();
            phoneVerificationService.sendVerificationCode(new HttpEvent() {
                @Override
                public void success(String data) {
                    runOnUiThread(() -> {
                        circularProgressButton.revertAnimation();
                        verificationCode.setVisibility(View.VISIBLE);
                        verificationCode.setEnabled(true);
                        circularProgressButton.setOnClickListener(v1 -> verifyCode(phoneVerificationService));
                    });
                }

                @Override
                public void failure(Exception e) {
                    reset();
                    showError();
                }
            });
        });
    }

    private void verifyCode(PhoneVerificationService phoneVerificationService){
        String code=verificationCode.getText().toString();
        animateLoadingButton();
        phoneVerificationService.verify(code, new HttpEvent() {
            @Override
            public void success(String data) {
                try{
                    HttpSuccessResponse httpSuccessResponse =new ObjectMapper().readValue(data, HttpSuccessResponse.class);
                    if(Objects.equals(httpSuccessResponse.getStatus(), "error")) throw new Exception("Verification failure");
                    httpClient.post("/api/user/save-info", initialSetupModel, new HttpEvent() {
                        @Override
                        public void success(String data) {
                            try{
                                Log.i(TAG, "success: "+data);
                                HttpSuccessResponse httpSuccessResponse=new ObjectMapper().readValue(data,HttpSuccessResponse.class);
                                if(httpSuccessResponse.getStatus().equals("error")) throw new Exception(httpSuccessResponse.getMessage());
                                NavigatorUtility.getInstance(appCompatActivity).switchToHomePage();
                            }catch (Exception e){
                                //no need to revert phone verification, just check user is photo verified
                                circularProgressButton.setOnClickListener(UserAccountSetupActivity.this);
                                showError("Your facial information may be incorrect.");
                                reset();
                            }
                        }

                        @Override
                        public void failure(Exception e) {
                            reset();
                            showError("Something went wrong with your number.");
                        }
                    });
                    initialSetupModel.setPhoneNumber(phoneNumber.getText().toString());
                }catch (Exception e){
                    reset();
                    showError("Something went wrong");
                }
            }

            @Override
            public void failure(Exception e) {
                showError("Verification failed. Is your code correct?");
            }
        });
    }


    private void showError(){
        showError("Verification failure. Please retry");
    }
    private void showError(String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                noFaceFoundError.setText(message);
                noFaceFoundError.setVisibility(View.VISIBLE);
                circularProgressButton.revertAnimation();
            }
        });
    }

    private void animateLoadingButton(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                circularProgressButton.startAnimation();
            }
        });
    }

    private void reset(){
        runOnUiThread(()->{
            circularProgressButton.revertAnimation();
            previewView.setVisibility(View.VISIBLE);
            facePreview.setVisibility(View.INVISIBLE);
        });
    }


}