package com.iishanto.contactbuddy.activities.home.components;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomePageTabPagerAdapter extends FragmentStateAdapter {
    public HomePageTabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new RecentContactsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
