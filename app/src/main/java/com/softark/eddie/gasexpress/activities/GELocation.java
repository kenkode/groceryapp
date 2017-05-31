package com.softark.eddie.gasexpress.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softark.eddie.gasexpress.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GELocation extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 15;
    private GoogleMap googleMap;
    private Marker marker;
    private GoogleApiClient googleApiClient;
    private boolean mPermissionGranted;
    private Location mLastKnownLocation;
    private static Place place;
    private LatLng mLatLng;

    private final int PLACE_AUTOCOMPLETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_georder_location);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Choose location");
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_AUTOCOMPLETE) {
            if(resultCode == RESULT_OK) {
                place = PlaceAutocomplete.getPlace(this, data);
                goTo(place.getLatLng());
            }
        }
    }

    private void setSelectedLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(GELocation.this, Locale.getDefault());
        List<Address> addresses = null;
        com.softark.eddie.gasexpress.models.Location location = new com.softark.eddie.gasexpress.models.Location();
        if(latLng != null) {
            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addresses != null) {
                String cityName = addresses.get(0).getAddressLine(0);
                location.setAddress(cityName);
                location.setType(1);
                location.setLat(latLng.latitude);
                location.setLng(latLng.longitude);
                Intent intent = new Intent();
                intent.putExtra("location", location);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }else {
                Toast.makeText(GELocation.this, "Please try again", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(GELocation.this, "Location not applicable", Toast.LENGTH_LONG).show();
        }
    }

    private void setResult() throws IOException {
        com.softark.eddie.gasexpress.models.Location location = new com.softark.eddie.gasexpress.models.Location();
        if(marker != null && place != null) {
            location.setAddress(String.valueOf(place.getAddress()));
            location.setType(1);
            location.setLat(place.getLatLng().latitude);
            location.setLng(place.getLatLng().longitude);
            Intent intent = new Intent();
            intent.putExtra("location", location);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else if(marker != null && mLatLng != null) {
            setSelectedLocation(mLatLng);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.my_location_map);
        mapFragment.getMapAsync(this);

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(1000);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }
        };


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
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mLatLng = latLng;
                goTo(latLng);
            }
        });
        getDeviceLocation();
    }

    private void goTo(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        if(marker != null) {
            marker.remove();
        }
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        marker = googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), DEFAULT_ZOOM));
        updateLocationUi();
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
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        }else {
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        updateLocationUi();
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
                    if(marker != null) {
                        setResult();
                    }else {
                        Toast.makeText(GELocation.this, "Please select a location", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.search_location:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }
}
