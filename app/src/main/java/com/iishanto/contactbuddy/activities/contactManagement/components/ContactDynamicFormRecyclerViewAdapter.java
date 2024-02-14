package com.iishanto.contactbuddy.activities.contactManagement.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.model.Phones;
import com.iishanto.contactbuddy.model.SaveContactModel;
import com.iishanto.contactbuddy.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactDynamicFormRecyclerViewAdapter extends RecyclerView.Adapter<ContactDynamicFormRecyclerViewAdapter.FormGroupViewHolder> {

    private final Context context;
    private int fieldCount=1;
    List <FormGroupViewHolder> viewHolders=new ArrayList<>();
    List<SaveContactModel> initialValues=new ArrayList<>();

    public ContactDynamicFormRecyclerViewAdapter(Context context,User user){
        this.context=context;
        List <Phones> phones= Arrays.asList(user.getPhones());
        for (Phones phone:phones){
            SaveContactModel saveContactModel=new SaveContactModel();
            saveContactModel.setPhone(phone.getNumber());
            saveContactModel.setName(user.getName());
            saveContactModel.setAliasTarget(phone);
            initialValues.add(saveContactModel);
        }
    }
    @NonNull
    @Override
    public FormGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_unit,parent,false);
        return new FormGroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FormGroupViewHolder holder, int position) {
        if (initialValues.size()>position&&initialValues.get(position)!=null){
            holder.initiate(initialValues.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return initialValues.size();
    }

    public List<SaveContactModel> getAliases(){
        return initialValues;
    }

    public void addNewField() {
        SaveContactModel saveContactModel=new SaveContactModel();
        initialValues.add(saveContactModel);
        notifyDataSetChanged();
    }
    public void remove(int position){
        if(viewHolders.size()>position){
            viewHolders.remove(position);
        }
    }


    public class FormGroupViewHolder extends RecyclerView.ViewHolder{
        EditText name;
        EditText phoneNumber;
        SaveContactModel saveContactModel;

        public FormGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            phoneNumber=itemView.findViewById(R.id.phone_number);

            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    saveContactModel.setName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            phoneNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    saveContactModel.setPhone(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        public SaveContactModel getSaveContactModel(){
            return saveContactModel;
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setPhone(String phone) {
            this.phoneNumber.setText(phone);
        }

        public void initiate(SaveContactModel saveContactModel) {
            this.saveContactModel=saveContactModel;
            setName(saveContactModel.getName());
            setPhone(saveContactModel.getPhone());
        }
    }
}
