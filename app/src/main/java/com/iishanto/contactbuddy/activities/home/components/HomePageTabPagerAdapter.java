package com.iishanto.contactbuddy.activities.home.components;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.iishanto.contactbuddy.activities.home.components.contactAlias.ContactAliasViewPagerFragment;
import com.iishanto.contactbuddy.activities.home.components.nearby.NearbyViewPagerFragment;

public class HomePageTabPagerAdapter extends FragmentStateAdapter {
    private static final String TAG="HOME_PAGE_TAB_PAGER_ADAPTER";
    AppCompatActivity appCompatActivity;
    public HomePageTabPagerAdapter(@NonNull FragmentActivity fragmentActivity,AppCompatActivity appCompatActivity) {
        super(fragmentActivity);
        this.appCompatActivity=appCompatActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.i(TAG, "createFragment: "+position);
        return position==0?new ContactAliasViewPagerFragment(appCompatActivity,position):new NearbyViewPagerFragment(appCompatActivity,position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
