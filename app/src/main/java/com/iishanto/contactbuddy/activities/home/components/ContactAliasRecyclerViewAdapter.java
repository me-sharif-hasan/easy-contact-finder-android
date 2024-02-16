package com.iishanto.contactbuddy.activities.home.components;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.events.ImageLoadedEvent;
import com.iishanto.contactbuddy.model.SaveContactModel;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.permissionManagement.PermissionManager;
import com.iishanto.contactbuddy.service.image.ImageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class ContactAliasRecyclerViewAdapter extends RecyclerView.Adapter<ContactAliasRecyclerViewAdapter.AliasHolder> {
    private static final String TAG="CONTACT_ALIAS_RECYCLER_VIEW_ADAPTER";
    AppCompatActivity appCompatActivity;
    List <Long> keys=new ArrayList<>();

    Map<Long,List<SaveContactModel>> friends=new HashMap<>();
    public ContactAliasRecyclerViewAdapter(AppCompatActivity appCompatActivity){
        this.appCompatActivity=appCompatActivity;
    }

    public void addAlias(SaveContactModel alias){
        if(alias.getPerson()!=null){
            if(!friends.containsKey(alias.getPerson().getId())||friends.get(alias.getPerson().getId())==null){
                friends.put(alias.getPerson().getId(),new ArrayList<>());
            }
            friends.get(alias.getPerson().getId()).add(alias);
            keys= new ArrayList<>(friends.keySet());
        }
    }

    @NonNull
    @Override
    public AliasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_home_page,parent,false);
        return new AliasHolder(view,appCompatActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull AliasHolder holder, int position) {
        if(keys==null||keys.size()<=position) return;
        holder.setIsRecyclable(false);
        List<SaveContactModel> aliases=friends.get(keys.get(position));
        if (aliases!=null&&aliases.size()>0){
            holder.setPerson(aliases.get(0).getPerson());
            holder.setAliases(aliases);
        }
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public void clear() {
        int size=keys.size();
        keys.clear();
        friends.clear();
        appCompatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeRemoved(0,size);
            }
        });
        Log.i(TAG, "clear: "+friends.size()+" "+keys.size());
    }

    public class AliasHolder extends RecyclerView.ViewHolder{
        AppCompatActivity appCompatActivity;
        View root;
        ViewGroup contactHolder;

        TextView name;
        ShimmerLayout dp;
        ImageService imageService;
        public AliasHolder(@NonNull View itemView,AppCompatActivity appCompatActivity) {
            super(itemView);
            root=itemView;
            name=root.findViewById(R.id.contact_name);
            imageService=new ImageService(appCompatActivity);
            dp=root.findViewById(R.id.contact_item_profile_avatar);
            contactHolder=itemView.findViewById(R.id.contact_alias_holder);
            this.appCompatActivity=appCompatActivity;
        }

        public void loadContacts(SaveContactModel saveContactModel){
            View view=LayoutInflater.from(appCompatActivity).inflate(R.layout.contact_alias_item, contactHolder,false);
            TextView number=view.findViewById(R.id.alias_phone_number);
            TextView name=view.findViewById(R.id.alias_name);
            Button callButton=view.findViewById(R.id.alias_call_button);

            number.setText(saveContactModel.getNumber());
            name.setText(saveContactModel.getName());

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+saveContactModel.getNumber()));//change the number
                    appCompatActivity.startActivity(callIntent);
                }
            });

            contactHolder.addView(view);
        }

        public void setPerson(User person) {
            Log.i(TAG, "setPerson: setting up the person. "+person.getName());
            name.setText(person.getName());
            ShapeableImageView pic= (ShapeableImageView) dp.getChildAt(0);
            imageService.urlToBitmap(person.getPicture(), new ImageLoadedEvent() {
                @Override
                public void success(Bitmap bitmap) {
                    pic.setImageBitmap(bitmap);
                }

                @Override
                public void failure(Exception e) {

                }
            });
        }

        public void setAliases(List<SaveContactModel> aliases) {
            Log.i(TAG, "setAliases: "+aliases.size());
            for (SaveContactModel saveContactModel:aliases){
                loadContacts(saveContactModel);
            }
        }
    }
}
