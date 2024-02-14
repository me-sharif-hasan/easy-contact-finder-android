package com.iishanto.contactbuddy.activities.home.components;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomePageTabPagerAdapter extends FragmentStateAdapter {
    AppCompatActivity appCompatActivity;
    public HomePageTabPagerAdapter(@NonNull FragmentActivity fragmentActivity,AppCompatActivity appCompatActivity) {
        super(fragmentActivity);
        this.appCompatActivity=appCompatActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new RecentContactsFragment(appCompatActivity);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
