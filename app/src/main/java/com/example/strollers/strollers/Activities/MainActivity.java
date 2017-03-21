package com.example.strollers.strollers.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.strollers.strollers.R;
import com.example.strollers.strollers.Utilities.PermissionUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.main_layout)
    RelativeLayout mLayout;
    @BindView(R.id.current_location)
    FloatingActionButton currLocButton;
    @BindView(R.id.workout)
    Button workoutButton;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private static final int LOCATION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        workoutButton.setOnClickListener(this);

        currLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findCurrentLocation();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        findCurrentLocation();
    }

    public void findCurrentLocation() {
        ArrayList<String> requestPermissions = PermissionUtility.shouldAskForPermissions(this, new String[]{PermissionUtility.LOCATIONPERMISSION});
        if (!requestPermissions.isEmpty()) {
            // Request permission
            for (String permission : requestPermissions) {
                if (PermissionUtility.shouldShowRational(this, permission)) {
                    PermissionUtility.showRational(this, mLayout);
                } else {
                    PermissionUtility.requestPermissions(this, new String[]{permission}, LOCATION_REQUEST_CODE);
                }
            }
        } else {
            // Mark current location on map
            onConnected(null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        findCurrentLocation();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // Using PermissionUtility.hasPermission causes error
        // Re-usable code cannot be achieved
        if (ActivityCompat.checkSelfPermission(this, PermissionUtility.LOCATIONPERMISSION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Add current location on map and zoom
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            LatLng currLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currLoc).title("Current Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 15.0f));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.workout:
                intent = new Intent(v.getContext(), RouteTypeActivity.class);
                break;
        }

        startActivity(intent);
    }
}