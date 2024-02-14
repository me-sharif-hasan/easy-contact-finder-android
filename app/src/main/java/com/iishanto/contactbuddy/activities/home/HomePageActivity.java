package com.iishanto.contactbuddy.activities.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.activities.NavigatorUtility;
import com.iishanto.contactbuddy.activities.home.components.HomePageTabPagerAdapter;
import com.iishanto.contactbuddy.activities.home.services.HomeActivityDataService;
import com.iishanto.contactbuddy.events.ImageLoadedEvent;
import com.iishanto.contactbuddy.events.UserLoadedEvent;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.service.image.ImageService;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    HomeActivityDataService dataService;

    ViewPager2 tabViewPager;
    TabLayout tabLayout;

    ShimmerLayout profilePicture;
    ShimmerLayout name;
    ImageService imageService;

    Button scanButton;

    private final String TAG="HOME_PAGE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        tabViewPager=findViewById(R.id.home_view_pager);
        tabLayout=findViewById(R.id.home_tabs);
        tabViewPager.setAdapter(new HomePageTabPagerAdapter(this,this));
        tabViewPager.setCurrentItem(0);
        dataService=new HomeActivityDataService(this);
        profilePicture=findViewById(R.id.contact_profile_avatar);
        name=findViewById(R.id.home_name);
        imageService=new ImageService(this);
        scanButton=findViewById(R.id.home_scan_contact_button);
        scanButton.setOnClickListener(this);
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
    }


    private void loadUserData(){
        name.startShimmerAnimation();
        profilePicture.startShimmerAnimation();
        dataService.getUserInfo(new UserLoadedEvent() {
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
        }
    }
}