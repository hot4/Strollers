package com.example.strollers.strollers.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.strollers.strollers.Constants.Constants;
import com.example.strollers.strollers.Helpers.MapHelper;
import com.example.strollers.strollers.Models.Destination;
import com.example.strollers.strollers.Models.Route;
import com.example.strollers.strollers.R;
import com.example.strollers.strollers.Utilities.PermissionUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_layout)
    RelativeLayout mLayout;
    @BindView(R.id.current_location)
    FloatingActionButton currLocButton;
    @BindView(R.id.workout)
    Button workoutButton;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_REQUEST_CODE = 0;

    private Marker mCurrentMarker;
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    private Marker mDestinationMarker;
    private Destination mDestinationLocation;

    private Route route;
    private boolean drawRoute = false;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createLocationRequest();
        // Create an instance of GoogleAPIClient
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /* Get Destination if previously was selected */
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mDestinationLocation = (Destination) bundle.get(Constants.DESTINATION);
                if (mDestinationLocation != null ) drawRoute = true;
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

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
            /* Request permission */
            for (String permission : requestPermissions) {
                if (PermissionUtility.shouldShowRational(this, permission)) {
                    PermissionUtility.showRational(this, mLayout);
                } else {
                    PermissionUtility.requestPermissions(this, new String[]{permission}, LOCATION_REQUEST_CODE);
                }
            }
        } else {
            updateUI();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /* Response method for accepting/declining permissions */
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        findCurrentLocation();
    }

    @Override
    public void onStart() {
        /* Connect api client when is application is actively running */
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        /* Disconnect api client when application is closed */
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            findCurrentLocation();
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        /* Set new location and time */
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        /* Mark map with current location when map finishes loading */
        if (null != mCurrentLocation) {
            mCurrentMarker = MapHelper.markCurrLocOnMap(mMap, mCurrentMarker, mCurrentLocation, getString(R.string.current_location));

            if (drawRoute) {
                drawRoute = false;
                mDestinationMarker = MapHelper.markDestOnMap(mMap, mDestinationMarker, mDestinationLocation, getString(R.string.destination_label));
                route = new Route(mCurrentLocation, mDestinationLocation);
                mMap.addPolyline(MapHelper.drawRoute(this, route));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        if (!mGoogleApiClient.isConnected()) return;
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onClick(View v) {
        /* Start activity based on item clicked */
        Intent intent = null;
        switch (v.getId()) {
            case R.id.workout:
                intent = new Intent(v.getContext(), RouteTypeActivity.class);
                break;
        }

        intent.putExtra(Constants.LOCATION, mCurrentLocation);
        startActivity(intent);
    }
}