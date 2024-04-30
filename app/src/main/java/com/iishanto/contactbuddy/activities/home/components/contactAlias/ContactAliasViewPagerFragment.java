package com.iishanto.contactbuddy.activities.home.components.contactAlias;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.activities.home.components.ContactAliasRecyclerViewAdapter;
import com.iishanto.contactbuddy.events.AliasLoadedEvent;
import com.iishanto.contactbuddy.model.SaveContactModel;
import com.iishanto.contactbuddy.service.contact.ContactService;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class ContactAliasViewPagerFragment extends Fragment {
    private static final String TAG="CONTACT_ALIAS_VIEW_PAGER_FRAGMENT";
    AppCompatActivity appCompatActivity;
    RecyclerView recyclerView;
    ShimmerLayout shimmerLayout;
    ContactAliasRecyclerViewAdapter contactAliasRecyclerViewAdapter;
    ContactService contactService;
    HttpClient httpClient;
    SwipeRefreshLayout swipeRefreshLayout;
    int tab=0;
    public ContactAliasViewPagerFragment(){}
    public ContactAliasViewPagerFragment(AppCompatActivity appCompatActivity, int position){
        this.appCompatActivity=appCompatActivity;
        tab=position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.home_contact_alias_list,container,false);
        recyclerView=view.findViewById(R.id.contact_alias_recycler_view);
        httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,recyclerView.getContext());
        contactAliasRecyclerViewAdapter=new ContactAliasRecyclerViewAdapter(appCompatActivity);

        swipeRefreshLayout=view.findViewById(R.id.alias_swipe_refresh_layout);
        recyclerView.setAdapter(contactAliasRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        contactService=new ContactService(recyclerView.getContext());
        init();
        return view;
    }


    private void init(){
        swipeRefreshLayout.setRefreshing(true);
        loadAllAliases();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh: REFRESHING");
                loadAllAliases();
            }
        });
    }

    private void loadAllAliases(){
        contactService.loadAllContactAliases(new AliasLoadedEvent() {
            @Override
            public void success(List<SaveContactModel> aliases) {
                Log.i(TAG, "success: "+aliases.get(0).getPerson().getName());
                contactAliasRecyclerViewAdapter.clear();
                for (SaveContactModel alias:aliases){
                    contactAliasRecyclerViewAdapter.addAlias(alias);
                }
                recyclerView.post(() -> contactAliasRecyclerViewAdapter.notifyDataSetChanged());
                swipeRefreshLayout.post(()-> swipeRefreshLayout.setRefreshing(false));
            }

            @Override
            public void failure(Exception e) {
                swipeRefreshLayout.post(()->swipeRefreshLayout.setRefreshing(false));
            }
        });
    }


}
