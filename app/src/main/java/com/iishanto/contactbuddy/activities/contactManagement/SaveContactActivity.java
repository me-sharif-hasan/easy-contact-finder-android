package com.iishanto.contactbuddy.activities.contactManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.activities.NavigatorUtility;
import com.iishanto.contactbuddy.activities.camera.components.ContactDynamicFormRecyclerViewAdapter;
import com.iishanto.contactbuddy.events.ImageLoadedEvent;
import com.iishanto.contactbuddy.model.ContactAliasModel;
import com.iishanto.contactbuddy.model.SaveContactModel;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.permissionManagement.PermissionManager;
import com.iishanto.contactbuddy.service.contact.ContactService;
import com.iishanto.contactbuddy.service.image.ImageService;

import java.util.List;
import java.util.Objects;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class SaveContactActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG="SAVE_CONTACT_ACTIVITY";
    User contactUser;

//    TextView name;
    TextView email;
//    TextView phoneNumber;
    CircularProgressButton saveButton;
    ShimmerLayout shimmerLayout;
    ImageService imageService;
    ContactService contactService;
    RecyclerView contactForms;
    ImageButton contactAddNewField;
    Bitmap bitmap;

    ContactDynamicFormRecyclerViewAdapter contactDynamicFormRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_contact);
        contactUser = (User) Objects.requireNonNull(getIntent().getExtras()).get("user");
        Log.i(TAG,"onCreate: ss: "+contactUser.toJsonNode().toPrettyString());
        Log.i(TAG, "onCreate: ss: "+contactUser.getName());
        Log.i(TAG, "onCreate: ss: "+contactUser.getPhones().length);
        contactDynamicFormRecyclerViewAdapter=new ContactDynamicFormRecyclerViewAdapter(this, contactUser);

        Log.i(TAG, "onCreate: "+ contactUser.getName());
        shimmerLayout=findViewById(R.id.contact_image_shimmer);
//        name=findViewById(R.id.name);
//        phoneNumber=findViewById(R.id.phone_number);
        contactForms=findViewById(R.id.contact_form_collection);
        contactAddNewField=findViewById(R.id.contact_add_new_field);
//        name.setText(user.getName());
        imageService=new ImageService(this);
        saveButton=findViewById(R.id.save_contact_button);
        contactService=new ContactService(this);
        new PermissionManager(this).askForPermissions();
        init();
    }


    private void init(){
        //setting phone numbers
        if(contactUser.getPhones()!=null&& contactUser.getPhones().length>0){
//            phoneNumber.setText(user.getPhones()[0].getNumber());
        }
        //setting photo
        shimmerLayout.startShimmerAnimation();
        ShapeableImageView shapeableImageView= (ShapeableImageView) shimmerLayout.getChildAt(0);
        imageService.urlToBitmap(contactUser.getPicture(), new ImageLoadedEvent() {
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
        //setting the contact form
        contactForms.setLayoutManager(new LinearLayoutManager(this));
        contactForms.setAdapter(contactDynamicFormRecyclerViewAdapter);
        // -> adding add new fields functionalities
        contactAddNewField.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==saveButton){
            try {
                List <SaveContactModel> numbers=contactDynamicFormRecyclerViewAdapter.getAliases();
                ContactAliasModel contactAliasModel=new ContactAliasModel();
                contactAliasModel.setAliases(numbers);
                contactAliasModel.setContact(contactUser);
                contactService.save(contactAliasModel,bitmap);
//                contactService.saveAllContacts(numbers,bitmap);
                Toast.makeText(this,"Contact saved",Toast.LENGTH_LONG).show();
                NavigatorUtility.getInstance(this).switchToHomePage();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (v==contactAddNewField){
            contactDynamicFormRecyclerViewAdapter.addNewField();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        shimmerLayout.stopShimmerAnimation();
    }
}