package com.iishanto.contactbuddy.activities.home.components;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.activities.home.components.contact.ContactListRecyclerViewAdapter;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.HttpSuccessResponse;
import com.iishanto.contactbuddy.model.PhoneVerificationModel;
import com.iishanto.contactbuddy.model.SaveContactModel;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.service.AppSecurityProvider;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.user.BasicUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecentContactsFragment extends Fragment {
    private final String TAG="RECENT_CONTACTS_FRAGMENT";
    BasicUserService basicUserService;
    ContactListRecyclerViewAdapter contactListRecyclerViewAdapter;
    RecyclerView recyclerView;
    AppCompatActivity appCompatActivity;
    public RecentContactsFragment(AppCompatActivity appCompatActivity) {
        this.appCompatActivity=appCompatActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_contacts, container, false);
        recyclerView=view.findViewById(R.id.contact_list_view);
        contactListRecyclerViewAdapter=new ContactListRecyclerViewAdapter(appCompatActivity);
        recyclerView.setAdapter(contactListRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        basicUserService=new BasicUserService(recyclerView.getContext());
        loadRecentContacts();
        return view;
    }

    private void loadRecentContacts(){
        User user= AppSecurityProvider.getInstance().getUser();
        if(user==null) return;
        basicUserService.getRecentContacts(new HttpEvent() {
            @Override
            public void success(String data) {
                Log.i(TAG, "success: recent"+data);
                try {
                    HttpSuccessResponse httpSuccessResponse=new ObjectMapper().readValue(data,HttpSuccessResponse.class);
                    if(httpSuccessResponse.getStatus().equals("error")){
                        failure(new Exception("ERROR: "+httpSuccessResponse.getMessage()));
                        return;
                    }
                    List <SaveContactModel> phoneVerificationModels=new ObjectMapper().convertValue(httpSuccessResponse.getData(), new TypeReference<List<SaveContactModel>>() {});
                    Log.i(TAG, "success: "+phoneVerificationModels);
                    List <User> userList=new ArrayList<>();
                    for (SaveContactModel pvm:phoneVerificationModels){
                        userList.add(pvm.getAliasOwner());
                    }
                    contactListRecyclerViewAdapter.setUserList(userList);
                    appCompatActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            contactListRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
                    Log.i(TAG, "success: Notified");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Exception e) {
                super.failure(e);
            }
        });
    }

}