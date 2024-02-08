package com.iishanto.contactbuddy.activities.home.components;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.activities.home.components.contact.ContactListRecyclerViewAdapter;

public class RecentContactsFragment extends Fragment {

    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_contacts, container, false);
        recyclerView=view.findViewById(R.id.contact_list_view);
        ContactListRecyclerViewAdapter contactListRecyclerViewAdapter=new ContactListRecyclerViewAdapter();
        recyclerView.setAdapter(contactListRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

}