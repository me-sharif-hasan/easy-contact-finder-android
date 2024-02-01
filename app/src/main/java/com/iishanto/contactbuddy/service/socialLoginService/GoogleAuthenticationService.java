package com.iishanto.contactbuddy.service.socialLoginService;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.events.UserAuthEvents;
import com.iishanto.contactbuddy.model.User;
import com.iishanto.contactbuddy.service.backendAuthService.BasicAuthenticator;
import com.iishanto.contactbuddy.service.backendAuthService.credential.GoogleAuthCredential;

import java.util.concurrent.Executor;

import okhttp3.OkHttpClient;

public class GoogleAuthenticationService{
    public static final String TAG="GOOGLE_AUTHENTICATION_SERVICE";
    AppCompatActivity appCompatActivity;
    private OkHttpClient okHttpClient;
    public GoogleAuthenticationService(AppCompatActivity appCompatActivity){
//        okHttpClient=Endpoints.getOkHttpClient(appCompatActivity);
        this.appCompatActivity=appCompatActivity;
    }
    public void google(UserAuthEvents userAuthEvents){
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(appCompatActivity.getString(R.string.web_client_id))
                .build();

        Log.i(TAG, "google: "+appCompatActivity.getString(R.string.web_client_id));
        GetCredentialRequest getCredentialRequest=new GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build();
        CredentialManager credentialManager=CredentialManager.create(appCompatActivity);
        Log.i(TAG, "google: Authenticating google");
        credentialManager.getCredentialAsync(appCompatActivity, getCredentialRequest, null, new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        }, new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
            @Override
            public void onResult(GetCredentialResponse getCredentialResponse) {

                String email = getCredentialResponse.getCredential().getData().getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID");
                String name = getCredentialResponse.getCredential().getData().getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_DISPLAY_NAME");
                String idToken = getCredentialResponse.getCredential().getData().getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN");

                Log.i(TAG, "onResult: "+email+" "+name+" "+idToken);

                BasicAuthenticator basicAuthenticator=new BasicAuthenticator();
                GoogleAuthCredential googleAuthCredential=new GoogleAuthCredential();
                googleAuthCredential.setEmail(email);
                googleAuthCredential.setToken(idToken);
                basicAuthenticator.auth(googleAuthCredential, new UserAuthEvents() {
                    @Override
                    public void onSuccess(User user) {
                        Log.i(TAG, "onSuccess: Google auth success");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.w(TAG, "onError: Google Auth err from backend:"+e.getLocalizedMessage() );
                    }

                    @Override
                    public void onLoading() {
                        Log.i(TAG, "onLoading: Google auth is sending data to backend");
                    }
                });

            }

            @Override
            public void onError(@NonNull GetCredentialException e) {
                Log.i(TAG, "onError: google failure: "+e.getLocalizedMessage());
//                userAuthEvents.onError("গুগল ব্যবহার করা যায়নি।");
            }
        });
    }

}