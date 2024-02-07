package com.iishanto.contactbuddy.activities.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.android.material.textfield.TextInputEditText;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.activities.NavigatorUtility;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.UserRegistrationModel;
import com.iishanto.contactbuddy.service.userRegistration.UserRegistrationService;

public class UserRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="USER_REGISTRATION_ACTIVITY";
    TextInputEditText name;
    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText confirmPassword;
    CircularProgressButton registrationButton;
    TextView error;
    AppCompatActivity context;

    UserRegistrationService userRegistrationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirm_password);
        registrationButton=findViewById(R.id.reg_register);
        error=findViewById(R.id.reg_error_text);
        context=this;
        userRegistrationService=new UserRegistrationService(context);

        name.setOnClickListener(this);
        email.setOnClickListener(this);
        password.setOnClickListener(this);
        confirmPassword.setOnClickListener(this);
        registrationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==registrationButton){
            registerUser();
        }
    }

    private void showError(String message){
        runOnUiThread(() -> {
            error.setVisibility(View.VISIBLE);
            error.setText(message);
        });
    }
    private void registerUser() {
        UserRegistrationModel userRegistrationModel=new UserRegistrationModel();
        if(name.getText()==null||name.getText().toString().length()==0){
            showError("You must provide your name");
            return;
        }
        userRegistrationModel.setName(name.getText().toString());

        if(email.getText()==null||email.getText().toString().length()==0){
            showError("You must provide your unique email");
            return;
        }
        userRegistrationModel.setEmail(email.getText().toString());

        if(password.getText()==null||password.getText().toString().length()==0){
            showError("Please enter your password.");
            return;
        }
        userRegistrationModel.setPassword(password.getText().toString());

        if(confirmPassword.getText()!=null&&password.getText()!=null&&!confirmPassword.getText().toString().equals(password.getText().toString())){
            showError("Password confirmation do not match");
            return;
        }
        error.setVisibility(View.INVISIBLE);
        userRegistrationModel.setConfirmPassword(confirmPassword.getText().toString());

        userRegistrationService.register(userRegistrationModel, new HttpEvent() {
            @Override
            public void success(String data) {
                Log.i(TAG, "success: Registration success "+data);
                NavigatorUtility.getInstance(context).switchToLoginPage("You have successfully registered. Please login");
            }

            @Override
            public void failure(Exception e) {
                showError("Registration failure.");
            }
        });

        Log.i(TAG, "registerUser: "+userRegistrationModel.toJsonNode().toString());
    }
}