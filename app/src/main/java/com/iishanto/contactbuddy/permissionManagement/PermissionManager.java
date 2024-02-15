package com.iishanto.contactbuddy.permissionManagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.Map;

public class PermissionManager {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE
    };

    private final ActivityResultLauncher<String[]> activityResultLauncher;
    private final FragmentActivity fragmentActivity;

    public PermissionManager(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;

        this.activityResultLauncher = fragmentActivity.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                this::handlePermissionResult
        );
    }

    public void askForPermissions() {
        // Check if the required permissions are granted
        if (checkPermissions()) {
            // Permissions are already granted, perform your logic here
            // For example, startCamera();
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
            if (entry.getKey() != null && entry.getKey().equals(Manifest.permission.CAMERA) && !entry.getValue()) {
                permissionGranted = false;
                break;
            }
        }

        if (!permissionGranted) {
            // Permissions denied, show a message or handle accordingly
            Toast.makeText(
                    fragmentActivity,
                    "Camera permission denied",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Permissions granted, perform your logic here
            // For example, startCamera();
        }
    }

    // Your additional logic and methods go here
}
