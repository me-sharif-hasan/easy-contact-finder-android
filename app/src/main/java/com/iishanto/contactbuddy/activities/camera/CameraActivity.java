
package com.iishanto.contactbuddy.activities.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.common.util.concurrent.ListenableFuture;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.activities.home.components.contact.ContactListRecyclerViewAdapter;
import com.iishanto.contactbuddy.events.UserSearchEvent;
import com.iishanto.contactbuddy.model.Base64ImageSearchModel;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.service.CameraService;
import com.iishanto.contactbuddy.service.user.BasicUserService;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG="CAMERA_ACTIVITY";
    PreviewView previewView;
    ImageCapture imageCapture;
    CameraService cameraService;
    CircularProgressButton circularProgressButton;
    BasicUserService basicUserService;
    BottomSheetDialog bottomSheetDialog;
    View sheet;
    RecyclerView recyclerView;
    ContactListRecyclerViewAdapter contactListRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        previewView=findViewById(R.id.camer_preview);
        cameraService=new CameraService(this);
        circularProgressButton=findViewById(R.id.camera_capture_button);
        circularProgressButton.setOnClickListener(this);
        basicUserService=new BasicUserService(this);
        contactListRecyclerViewAdapter=new ContactListRecyclerViewAdapter(this);
        ListenableFuture<ProcessCameraProvider> processCameraProviderListenableFuture=ProcessCameraProvider.getInstance(this);
        processCameraProviderListenableFuture.addListener(()->{
            try {
                cameraService.startCameraX(processCameraProviderListenableFuture.get(),previewView,imageCapture,true);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, ContextCompat.getMainExecutor(this));


        bottomSheetDialog=new BottomSheetDialog(CameraActivity.this);
        sheet= LayoutInflater.from(getApplicationContext()).inflate(R.layout.phone_number_search_result_bottom_sheet, (ViewGroup) sheet);
        recyclerView=sheet.findViewById(R.id.phone_number_result);
        recyclerView.setAdapter(contactListRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomSheetDialog.setContentView(sheet);
    }

    @Override
    public void onClick(View v) {
        if(v==circularProgressButton){
            circularProgressButton.startAnimation();
            Bitmap bitmap=previewView.getBitmap();
            searchUserByImage(bitmap);
        }
    }

    private void searchUserByImage(final Bitmap bitmap){
        new Thread(() -> {
//            Bitmap bitmap=UtilityAndConstantsProvider.createBlackAndWhite(bitmap_color);
            String base64 = UtilityAndConstantsProvider.convertBitmapToBase64(bitmap);
            Base64ImageSearchModel imageSearchModel = new Base64ImageSearchModel();
            imageSearchModel.setBase_64_encoded_string(base64);
            basicUserService.searchUserByImage(imageSearchModel, new UserSearchEvent() {
                @Override
                public void success(List<User> userList) {
                    circularProgressButton.revertAnimation();
                    if(userList!=null){
                        contactListRecyclerViewAdapter.setUserList(userList);
                        bottomSheetDialog.show();
                    }
                }

                @Override
                public void failure(Exception e) {
                    circularProgressButton.revertAnimation();
                }
            });
        }).start();
    }

    @Override
    public void onDetachedFromWindow() {
        bottomSheetDialog.dismiss();
    }
}