package com.iishanto.contactbuddy.permissionManagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.Map;

public class PermissionManager {

    public interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
    }

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private  ActivityResultLauncher<String[]> activityResultLauncher;
    private  FragmentActivity fragmentActivity;
    private  PermissionCallback permissionCallback;
    public PermissionManager(FragmentActivity fragmentActivity){
        init(fragmentActivity,null);
    }
    public PermissionManager(FragmentActivity fragmentActivity,PermissionCallback permissionCallback){
        init(fragmentActivity,permissionCallback);
    }

    public void init(FragmentActivity fragmentActivity, PermissionCallback permissionCallback) {
        this.fragmentActivity = fragmentActivity;
        this.permissionCallback = permissionCallback;

        this.activityResultLauncher = fragmentActivity.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> permissions) {
                        handlePermissionResult(permissions);
                    }
                }
        );
    }

    public void askForPermissions() {
        // Check if the required permissions are granted
        if (checkPermissions()&&permissionCallback!=null) {
            // Permissions are already granted, notify callback
            permissionCallback.onPermissionGranted();
        } else {
            // Request permissions
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        // Check if all the required permissions are granted
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(fragmentActivity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        // Request permissions
        activityResultLauncher.launch(REQUIRED_PERMISSIONS);
    }

    private void handlePermissionResult(Map<String, Boolean> permissions) {
        // Handle Permission granted/rejected
        boolean permissionGranted = true;
        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
            if (!entry.getValue()) {
                permissionGranted = false;
                break;
            }
        }

        if (permissionGranted&&permissionCallback!=null) {
            // Permissions granted, notify callback
            permissionCallback.onPermissionGranted();
        } else if(permissionCallback!=null) {
            // Permissions denied, notify callback
            permissionCallback.onPermissionDenied();
        }
    }

    // Your additional logic and methods go here
}
