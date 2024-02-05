package com.iishanto.contactbuddy.activities.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.android.material.textfield.TextInputEditText;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.activities.setup.UserAccountSetupActivity;
import com.iishanto.contactbuddy.events.UserAuthEvents;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.service.AppSecurityProvider;
import com.iishanto.contactbuddy.service.backendAuthService.BasicAuthenticator;
import com.iishanto.contactbuddy.service.backendAuthService.credential.BackendCredential;
import com.iishanto.contactbuddy.service.backendAuthService.credential.ClassicCredential;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final String TAG="LOGIN_ACTIVITY";
    TextInputEditText email;
    TextInputEditText password;
    CircularProgressButton loginButton;
    CircularProgressButton googleLoginButton;
    CircularProgressButton googleRegistrationButton;
    TextView loginErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG, "onCreate: Authenticating using google");
        googleRegistrationButton =findViewById(R.id.register_with_google);
        googleRegistrationButton.setOnClickListener(this);
        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        loginButton =findViewById(R.id.login_email_button);
        loginButton.setOnClickListener(this);
        loginErrorText=findViewById(R.id.login_error_text);
        googleLoginButton=findViewById(R.id.login_with_google);
        googleLoginButton.setOnClickListener(this);
    }

    public void login(){
        try{
            changeLoginButtonAnimationState(true);
            String email_input= Objects.requireNonNull(email.getText()).toString();
            String email_password= Objects.requireNonNull(password.getText()).toString();
            BasicAuthenticator basicAuthenticator=new BasicAuthenticator();
            BackendCredential userLoginCredential=new ClassicCredential(email_input,email_password);
            basicAuthenticator.auth(userLoginCredential, new UserAuthEvents() {
                @Override
                public void onSuccess(User user) {
                    changeLoginButtonAnimationState(false);
                    doLogin(user);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    loginError();
                }

                @Override
                public void onLoading() {

                }
            });
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public void loginError(){
        runOnUiThread(() -> {
            loginErrorText.setVisibility(View.VISIBLE);
            googleLoginButton.revertAnimation();
            googleRegistrationButton.revertAnimation();
            loginButton.revertAnimation();
        });
    }

    private void changeLoginButtonAnimationState(Boolean state){
        runOnUiThread(() -> {
            loginButton.startAnimation();
            loginErrorText.setVisibility(View.INVISIBLE);
            if(state) {
                googleLoginButton.startAnimation();
                googleRegistrationButton.startAnimation();
                loginButton.startAnimation();
            }
            else {
                loginButton.revertAnimation();
                googleLoginButton.revertAnimation();
                googleRegistrationButton.revertAnimation();
            }
        });
    }
    private void loginWithGoogle(){
        googleLoginButton.startAnimation();
        googleRegistrationButton.startAnimation();
        GoogleAuthentication googleAuthentication=new GoogleAuthentication(this);
        googleAuthentication.google(new UserAuthEvents() {
            @Override
            public void onSuccess(User user) {
                changeLoginButtonAnimationState(false);
                doLogin(user);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                loginError();
            }

            @Override
            public void onLoading() {
                Log.i(TAG, "onLoading: ");
                super.onLoading();
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v== loginButton){
            login();
        }else if(v==googleLoginButton){
            loginWithGoogle();
        }else if(v== googleRegistrationButton){
            loginWithGoogle();
        }
    }


    private void doLogin(User user){
        Log.i(TAG, "doLogin: "+ AppSecurityProvider.getInstance().getSecurityToken());
        if(user.getPicture()==null||user.getPhones()==null||user.getPhones().length==0){
            //user is newly registered
            Intent i=new Intent(this, UserAccountSetupActivity.class);
            startActivity(i);
            finish();
        }else{
            //user is old
        }
    }
}