package com.softark.eddie.gasexpress;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;

public class GELocation extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 6;
    private GoogleMap googleMap;
    private Marker marker;
    private GoogleApiClient googleApiClient;
    private boolean mPermissionGranted;
    private Location mLastKnownLocation;
    private final LatLng mDefaultLocation = new LatLng(1.2921, 36.8219);
    private LocationListener locationListener;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_georder_location);

        button = (Button) findViewById(R.id.select_my_place);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastKnownLocation != null) {
                    setCurrLocation();
                }else {
                    if(ContextCompat.checkSelfPermission(GELocation.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mPermissionGranted = true;
                    }else {
                        ActivityCompat.requestPermissions(GELocation.this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }

                    if(mPermissionGranted) {
                        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    }
                    setCurrLocation();
                }
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();
    }

    public void setCurrLocation() {
        Geocoder geocoder = new Geocoder(GELocation.this, Locale.getDefault());
        List<Address> addresses = null;
        if(mLastKnownLocation != null) {
            try {
                addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
            com.softark.eddie.gasexpress.models.Location location = new com.softark.eddie.gasexpress.models.Location();
            location.setAddress(cityName);
            location.setType(1);
            location.setLat(mLastKnownLocation.getLatitude());
            location.setLng(mLastKnownLocation.getLongitude());
            Intent intent = new Intent();
            intent.putExtra("location", location);
            setResult(Activity.RESULT_OK, intent);
        }else {
            Toast.makeText(this, "Location is turned off", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public void setResult() throws IOException {
        if(marker != null) {
            LatLng position = marker.getPosition();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
            com.softark.eddie.gasexpress.models.Location location = new com.softark.eddie.gasexpress.models.Location();
            location.setAddress(cityName);
            location.setType(1);
            location.setLat(position.latitude);
            location.setLng(position.longitude);
            Intent intent = new Intent();
            intent.putExtra("location", location);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            setResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.my_location_map);
        mapFragment.getMapAsync(this);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                MarkerOptions options = new MarkerOptions();
//                options.position(latLng);
//                googleMap.addMarker(options);
                goTo(latLng);

            }
        };

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(1000);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, locationListener);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        final MarkerOptions options = new MarkerOptions();

        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(marker != null) {
                    marker.remove();
                }

                options.position(latLng);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                options.draggable(true);
                marker = googleMap.addMarker(options);
            }
        });

        updateLocationUi();

        getDeviceLocation();

    }

    private void goTo(LatLng latLng) {
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

    }

    private void getDeviceLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mPermissionGranted = true;
        }else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if(mPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }

        if(mLastKnownLocation != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 15));
        }else {
            Log.d("LOCATION", "Last known is unknown");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void updateLocationUi() {
        if(googleMap == null) {
            return;
        }

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mPermissionGranted = true;
        }else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if(mPermissionGranted) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }else {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionGranted = true;
                }
        }
        updateLocationUi();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("LOCATION", connectionResult.getErrorMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_selecting_location:
                try {
                    setResult();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }
}
