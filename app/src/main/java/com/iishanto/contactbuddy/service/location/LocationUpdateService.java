package com.iishanto.contactbuddy.service.location;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.LocationModel;
import com.iishanto.contactbuddy.permissionManagement.PermissionManager;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;

import java.security.Provider;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LocationUpdateService extends Service {
    private static final String TAG="LOCATION_MODEL";
    public static Double latitude;
    public static Double longitude;
    private final HttpClient httpClient;

    public LocationUpdateService(){
        httpClient=new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl,this.getBaseContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("JJJJ", "kkk");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("JJJ", "onStartCommand: ppp");


        Notification notification = new NotificationCompat.Builder(this, String.valueOf(101)).
                setContentTitle("Contact Buddy")
                .setContentText("Your location details is updating!")
                .setSmallIcon(R.drawable.baseline_admin_panel_settings_24)
                .build();
        startForeground(101, notification);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i("LOCATION UPDATING SERVICE","UPDATING LOCATION");
            try{
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    double previousLat=0;
                    double previousLon=0;
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        latitude=location.getLatitude();
                        longitude=location.getLongitude();
                        double dist=Math.sqrt(Math.pow((latitude-previousLat),2)+Math.pow((longitude-previousLon),2));
                        if(dist<0) return;
                        previousLat=latitude;
                        previousLon=longitude;
                        LocationModel locationModel=new LocationModel();
                        locationModel.setLatitude(latitude);
                        locationModel.setLongitude(longitude);
                        locationModel.setDeviceKey("xxx-xxx-xxx");
                        httpClient.post("/api/location/update", locationModel , new HttpEvent() {
                            @Override
                            public void success(String data) {
                                Log.i(TAG, "success: "+data);
                            }

                            @Override
                            public void failure(Exception e) {
                                e.printStackTrace();
                            }
                        });
                        Log.i("LOCATION UPDATED: ", "onLocationChanged: "+location.getLongitude()+" "+location.getLatitude());
                    }

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {

                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                });
            }catch (Throwable e){
                e.printStackTrace();
            }
        }

        return START_STICKY;
    }
}
