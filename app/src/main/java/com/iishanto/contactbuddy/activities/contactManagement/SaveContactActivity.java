package com.iishanto.contactbuddy.activities.contactManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.activities.NavigatorUtility;
import com.iishanto.contactbuddy.events.ImageLoadedEvent;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.permissionManagement.PermissionManager;
import com.iishanto.contactbuddy.service.contact.ContactService;
import com.iishanto.contactbuddy.service.image.ImageService;

import java.util.ArrayList;
import java.util.Objects;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class SaveContactActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG="SAVE_CONTACT_ACTIVITY";
    User user;

    TextView name;
    TextView email;
    TextView phoneNumber;
    CircularProgressButton saveButton;
    ShimmerLayout shimmerLayout;
    ImageService imageService;
    ContactService contactService;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_contact);
        user= (User) Objects.requireNonNull(getIntent().getExtras()).get("user");
        Log.i(TAG, "onCreate: "+user.getName());
        shimmerLayout=findViewById(R.id.contact_image_shimmer);
        name=findViewById(R.id.name);
        phoneNumber=findViewById(R.id.phone_number);
        name.setText(user.getName());
        imageService=new ImageService(this);
        saveButton=findViewById(R.id.save_contact_button);
        contactService=new ContactService(this);
        new PermissionManager(this).askForPermissions();
        init();
    }


    private void init(){
        //setting phone numbers
        if(user.getPhones()!=null&&user.getPhones().length>0){
            phoneNumber.setText(user.getPhones()[0].getNumber());
        }
        //setting photo
        shimmerLayout.startShimmerAnimation();
        ShapeableImageView shapeableImageView= (ShapeableImageView) shimmerLayout.getChildAt(0);
        imageService.urlToBitmap(user.getPicture(), new ImageLoadedEvent() {
            @Override
            public void success(Bitmap bitmap) {
                try{
                    shapeableImageView.setImageBitmap(bitmap);
                    SaveContactActivity.this.bitmap=bitmap;
                    shimmerLayout.stopShimmerAnimation();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Exception e) {
                e.printStackTrace();
                shimmerLayout.stopShimmerAnimation();
            }
        });


        saveButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==saveButton){
            addContact(name.getText().toString(),phoneNumber.getText().toString(),bitmap);
        }
    }

    private void addContact(String name, String phone,Bitmap bitmap) {
            contactService.saveContact(name,phone,bitmap);
        Toast.makeText(this,"Contact saved",Toast.LENGTH_LONG).show();
        NavigatorUtility.getInstance(this).switchToHomePage();
    }

    @Override
    public void onDetachedFromWindow() {
        shimmerLayout.stopShimmerAnimation();
    }
}