package com.iishanto.contactbuddy.activities.camera.components.contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.activities.NavigatorUtility;
import com.iishanto.contactbuddy.events.ImageLoadedEvent;
import com.iishanto.contactbuddy.model.Phones;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.service.image.ImageService;

import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class ContactListRecyclerViewAdapter extends RecyclerView.Adapter<ContactListRecyclerViewAdapter.ContactViewHolder> {
    private final String TAG="CONTACT_LIST_RECYCLER_VIEW_ADAPTER";
    List <User> userList=new ArrayList<>();
    Context context;
    public ContactListRecyclerViewAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContactUser(userList.get(position));
        User user=userList.get(position);
        holder.setName(user.getName());
        holder.setPicture(user.getPicture());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        Log.i(TAG, "setUserList: Loading users");
        for (User user :
                userList) {
            Log.i(TAG, "user list is: "+user.getName());
        }
    }

    @Override
    public void onViewRecycled(@NonNull ContactViewHolder holder) {
        holder.shimmerLayout.startShimmerAnimation();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ContactViewHolder holder) {
        holder.shimmerLayout.stopShimmerAnimation();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageService imageService;
        View view;

        ShimmerLayout shimmerLayout;
        User contactUser;

        ImageButton saveButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            shimmerLayout = (ShimmerLayout) itemView.findViewById(R.id.contact_item_profile_avatar);
            shimmerLayout.startShimmerAnimation();
            imageService=new ImageService(ContactListRecyclerViewAdapter.this.context);
            saveButton=view.findViewById(R.id.contact_item_save_button);
            saveButton.setOnClickListener(v -> saveContact());
        }

        public void saveContact(){
            if (contactUser ==null) {
                if(context instanceof AppCompatActivity) ((AppCompatActivity) context).runOnUiThread(() -> Toast.makeText(context,"Error saving contact.",Toast.LENGTH_LONG).show());
                return;
            }

            if(context instanceof AppCompatActivity){

                for (Phones p: contactUser.getPhones()){
                    Log.i("NAVIGATOR", "onCreate vgb 1: "+p.getId());
                }
                NavigatorUtility.getInstance((AppCompatActivity) context).switchToNumberSavingPage(contactUser);
            }
        }

        public void setName(String name){
            TextView v=view.findViewById(R.id.contact_name);
            v.setText(name);
        }


        public void setPicture(String picture) {
            ShapeableImageView shapeableImageView= (ShapeableImageView) shimmerLayout.getChildAt(0);
            imageService.urlToBitmap(picture, new ImageLoadedEvent() {
                @Override
                public void success(Bitmap bitmap) {
                    Log.i("TAG", "success: setting bitmap");
                    shapeableImageView.setImageBitmap(bitmap);
                    shimmerLayout.stopShimmerAnimation();
                }

                @Override
                public void failure(Exception e) {
                    //failure
                    e.printStackTrace();
                }
            });
        }

        public void setContactUser(User contactUser) {
            this.contactUser = contactUser;
        }

        public User getContactUser() {
            return contactUser;
        }
    }
}
