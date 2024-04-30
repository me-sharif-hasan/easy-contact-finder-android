package com.iishanto.contactbuddy.activities.home.components.nearby;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.android.ui.IconGenerator;
import com.iishanto.contactbuddy.R;
import com.iishanto.contactbuddy.UtilityAndConstantsProvider;
import com.iishanto.contactbuddy.events.HttpEvent;
import com.iishanto.contactbuddy.model.LocationModel;
import com.iishanto.contactbuddy.model.Model;
import com.iishanto.contactbuddy.service.http.HttpClient;
import com.iishanto.contactbuddy.service.http.OkHttpClientImpl;
import com.iishanto.contactbuddy.service.location.LocationUpdateService;

import java.util.List;
import java.util.Objects;

public class NearbyViewPagerFragment extends Fragment {
    private static final String TAG = "NEARBY_VIEW_PAGER_FRAGMENT";
    AppCompatActivity parentCompactActivity;
    MapView mapView;
    int tab;
    HttpClient httpClient;
    GoogleMap currentGoogleMap;
    SwipeRefreshLayout swipeRefreshLayout;
    BottomSheetDialog bottomSheetDialog;
    public NearbyViewPagerFragment(AppCompatActivity appCompatActivity, int position) {
        parentCompactActivity = appCompatActivity;
        tab = position;
        httpClient = new OkHttpClientImpl(UtilityAndConstantsProvider.baseUrl, appCompatActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(parentCompactActivity).inflate(R.layout.fragment_nearby_contact_google_map, container, false);
        swipeRefreshLayout=v.findViewById(R.id.pull_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::locationUpdate);
        bottomSheetDialog=new BottomSheetDialog(parentCompactActivity);
        bottomSheetDialog.setContentView(R.layout.activity_splash_screen);
        Log.i(TAG, "onCreateView: Getting location update");
        mapView = v.findViewById(R.id.nearby_map);
        Log.i(TAG, "onCreateView: LOADING GMAP");
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(
                googleMap -> {
                    currentGoogleMap = googleMap;
                    if (LocationUpdateService.longitude != null && LocationUpdateService.latitude != null) {
                        LatLng latLng = new LatLng(LocationUpdateService.latitude, LocationUpdateService.longitude);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                        swipeRefreshLayout.setRefreshing(true);
                        locationUpdate();
                    }
                }
        );


        return v;
    }

    //listen to location update
    void locationUpdate() {
        Log.i(TAG, "locationUpdate: getting nearby");
        httpClient.get("/api/location/nearby", new HttpEvent() {
            @Override
            public void success(String data) {
                Log.i(TAG, "nearby-contacts: " + data);
                List<LocationModel> locationModels = null;
                try {
                    Log.i(TAG, "success: loading users");
                    locationModels = new ObjectMapper().readValue(data, new TypeReference<List<LocationModel>>() {
                    });
                    Log.i(TAG, "success: loaded");
                    for (LocationModel locationModel : locationModels) {
                        Log.i(TAG, "success: checking google map");
                        if (currentGoogleMap != null) {
                            Log.i(TAG, "success: okay google");
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(locationModel.getLatitude(), locationModel.getLongitude()));
                            parentCompactActivity.runOnUiThread(() -> {
                                Marker marker = currentGoogleMap.addMarker(markerOptions);
                                if (marker != null) {
                                    IconGenerator iconGenerator = new IconGenerator(parentCompactActivity);
                                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(locationModel.getName())));
                                    currentGoogleMap.setOnMarkerClickListener(marker1 -> {
                                        //request for user info
                                        bottomSheetDialog.show();
                                      return true;
                                    });
                                }
                            });
                            Log.i(TAG, "success: marker added");
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
