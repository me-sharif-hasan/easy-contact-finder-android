package com.iishanto.contactbuddy.activities.home.components.contact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iishanto.contactbuddy.R;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class ContactListRecyclerViewAdapter extends RecyclerView.Adapter<ContactListRecyclerViewAdapter.ContactViewHolder> {

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 500;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            ShimmerLayout shimmerText = (ShimmerLayout) itemView.findViewById(R.id.contact_item_profile_avatar);
            shimmerText.startShimmerAnimation();
        }


    }
}
