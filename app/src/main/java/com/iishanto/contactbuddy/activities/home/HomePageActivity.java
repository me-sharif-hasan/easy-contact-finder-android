package com.iishanto.contactbuddy.activities.home;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.activities.NavigatorUtility;
import com.iishanto.contactbuddy.activities.home.components.HomePageTabPagerAdapter;
import com.iishanto.contactbuddy.activities.home.services.HomeActivityDataService;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.events.ImageLoadedEvent;
import com.iishanto.contactbuddy.events.UserLoadedEvent;
import com.iishanto.contactbuddy.model.ProfilePictureUploadModel;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.permissionManagement.PermissionManager;
import com.iishanto.contactbuddy.service.image.ImageService;
import com.iishanto.contactbuddy.service.location.LocationUpdateService;
import com.iishanto.contactbuddy.service.storage.FileService;
import com.iishanto.contactbuddy.service.user.BasicUserService;

import java.io.InputStream;
import java.time.Duration;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    HomeActivityDataService dataService;

    ViewPager2 tabViewPager;
    TabLayout tabLayout;

    ShimmerLayout profilePicture;
    ShimmerLayout name;
    ImageService imageService;
    Button logoutButton;

    Button scanButton;
    BasicUserService userService;
    CircularProgressButton uploadImageButton;
    ProgressBar progressBar;
    FileService fileService=new FileService();

    ActivityResultLauncher<Intent> activityResultLauncher=
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            uploadProfilePicture(o);
                        }
                    }
            );
    private final String TAG="HOME_PAGE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        tabViewPager=findViewById(R.id.home_view_pager);
        tabViewPager.setUserInputEnabled(false);
        tabLayout=findViewById(R.id.home_tabs);
        tabViewPager.setAdapter(new HomePageTabPagerAdapter(this,this));
        tabViewPager.setCurrentItem(0);
        dataService=new HomeActivityDataService(this);
        profilePicture=findViewById(R.id.contact_profile_avatar);
        uploadImageButton=findViewById(R.id.upload_new_photo_button);
        name=findViewById(R.id.home_name);
        imageService=new ImageService(this);
        scanButton=findViewById(R.id.home_scan_contact_button);
        logoutButton=findViewById(R.id.home_logout_button);
        userService=new BasicUserService(this);
        logoutButton.setOnClickListener(this);
        scanButton.setOnClickListener(this);
        uploadImageButton.setOnClickListener(this);
        progressBar=findViewById(R.id.dp_uploading_profile_picture);
        progressBar.setVisibility(View.INVISIBLE);
        new TabLayoutMediator(tabLayout, tabViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Recent contacts");
                        break;
                    case 1:
                        tab.setText("Nearby contacts");
                        break;
                }
            }
        }).attach();
        loadUserData();


        Intent i=new Intent(this,LocationUpdateService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "onCreate: CREATING FOREGROUND SERVICE");
            startForegroundService(i);
        }else{
            startService(i);
        }
    }


    private void loadUserData(){
        name.startShimmerAnimation();
        profilePicture.startShimmerAnimation();
        userService.getUserInfo(new UserLoadedEvent() {
            @Override
            public void success(User user) {
                Log.i(TAG, "success: "+user.toJsonNode().toString());
                if(name.getChildAt(0) instanceof TextView){
                    ((TextView) name.getChildAt(0)).setText(user.getName());
                    name.stopShimmerAnimation();
                }

                if(profilePicture.getChildAt(0) instanceof ImageView){
                    if(user.getPicture()!=null){
                        imageService.urlToBitmap(user.getPicture(), new ImageLoadedEvent() {
                            @Override
                            public void success(Bitmap bitmap) {
                                ((ImageView)profilePicture.getChildAt(0)).setImageBitmap(bitmap);
                                profilePicture.stopShimmerAnimation();
                            }

                            @Override
                            public void failure(Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }

            @Override
            public void failure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==scanButton){
            NavigatorUtility.getInstance(this).switchToContactFinderWithScanPage();
        }else if(v==logoutButton){
            userService.logout();
        }else if(v==uploadImageButton){
            Intent i=new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(i);
        }
    }

    private void uploadProfilePicture(ActivityResult o){
        Log.i(TAG+" UPLOAD OKAY", String.valueOf(Activity.RESULT_OK));
        if(o.getResultCode()== Activity.RESULT_OK){
            try {
                Uri photoUri=o.getData().getData();
                final InputStream imageStream = getContentResolver().openInputStream(photoUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                progressBar.setVisibility(View.VISIBLE);
                userService.uploadProfilePicture(selectedImage,new HttpEvent() {
                    @Override
                    public void success(String data) {
                        runOnUiThread(()->{
                            progressBar.setVisibility(View.INVISIBLE);
                            ImageView imageView= (ImageView) profilePicture.getChildAt(0);
                            imageView.setImageBitmap(selectedImage);
                            Toast.makeText(getApplicationContext(),"Profile picture updated successfully",Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override
                    public void failure(Exception e) {
                        runOnUiThread(()->{
                            Toast.makeText(getApplicationContext(),"Profile picture can not be updated",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        });
                    }
                });
            }catch (Exception e){

            }
        }
    }
}