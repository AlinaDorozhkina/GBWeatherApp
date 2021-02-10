package ru.alinadorozhkina.gbweatherapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import ru.alinadorozhkina.gbweatherapp.DB.FavViewModel;
import ru.alinadorozhkina.gbweatherapp.DB.Favourites;
import ru.alinadorozhkina.gbweatherapp.helper.Keys;
import ru.alinadorozhkina.gbweatherapp.screens.weather.WeatherDescription;

public class WeatherMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private Marker marker;

    private static final int CHECK_SETTINGS_CODE = 111;
    private static final int REQUEST_LOCATION_PERMISSION = 222;
    private static final String TAG = WeatherMapsActivity.class.getSimpleName();

    private FavViewModel viewModel;

    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;

    private boolean isLocationUpdatesActive;
    private EditText textLat;
    private EditText textLon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(Keys.THEME, true)) {
            setTheme(R.style.AppDarkTheme);
        }
        setContentView(R.layout.activity_weather_maps);
        initView();
        initToolBar();
        addFavouritesCitiesOnMap();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initView() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance()).create(FavViewModel.class);
        textLat = findViewById(R.id.edit_text_lat);
        textLon = findViewById(R.id.edit_text_lon);
        MaterialButton check_weather_button = findViewById(R.id.check_weather_button);
        check_weather_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textLat.getText().length() > 0 && textLon.getText().length() > 0) {
                    Intent intent = new Intent(WeatherMapsActivity.this, WeatherDescription.class);
                    intent.putExtra(Keys.LAT, Double.parseDouble(textLat.getText().toString()));
                    intent.putExtra(Keys.LON, Double.parseDouble(textLon.getText().toString()));
                    startActivity(intent);
                } else {
                    Toast.makeText(WeatherMapsActivity.this, getResources().getString(R.string.enter_coordinates),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        MaterialButton find_me_button = findViewById(R.id.find_me_button);
        find_me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(WeatherMapsActivity.this);
                settingsClient = LocationServices.getSettingsClient(WeatherMapsActivity.this);
                buildLocationRequest();
                buildLocationCallBack();
                buildLocationSettingsRequest();
                startLocationUpdates();
            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbarWeatherMap);
        setActionBar(toolbar);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addFavouritesCitiesOnMap() {
        viewModel.getAllFavourites().observe(this, new Observer<List<Favourites>>() {
            @Override
            public void onChanged(List<Favourites> favourites) {
                if (favourites != null) {
                    Log.v(TAG, " размер " + favourites.size());
                    for (int i = 0; i < favourites.size(); i++) {
                        String title = favourites.get(i).getCityName();
                        LatLng location = new LatLng(favourites.get(i).getLat(), favourites.get(i).getLon());
                        Log.v(TAG, " координаты " + location.latitude + location.longitude);
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(title)
                                .icon(BitmapDescriptorFactory.defaultMarker()));
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (currentLocation != null) {
            LatLng userLocation = new LatLng(currentLocation.getLatitude(),
                    currentLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(userLocation).title(getResources().getString(R.string.your_location)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        }
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarker(latLng);
            }
        });
    }

    private void addMarker(LatLng location) {
        if (marker != null) {
            marker.remove();
        }
        marker = googleMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker()));
        textLat.setText(String.valueOf(location.latitude));
        textLon.setText(String.valueOf(location.longitude));
    }

    private void startLocationUpdates() {
        isLocationUpdatesActive = true;
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this,
                        new OnSuccessListener<LocationSettingsResponse>() {
                            @Override
                            public void onSuccess(
                                    LocationSettingsResponse locationSettingsResponse) {
                                if (ActivityCompat.checkSelfPermission(
                                        WeatherMapsActivity.this,
                                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                                        PackageManager.PERMISSION_GRANTED &&
                                        ActivityCompat
                                                .checkSelfPermission(
                                                        WeatherMapsActivity.this,
                                                        Manifest.permission
                                                                .ACCESS_COARSE_LOCATION) !=
                                                PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                fusedLocationClient.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper()
                                );
                                updateLocationUi();
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes
                                    .RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException resolvableApiException =
                                            (ResolvableApiException) e;
                                    resolvableApiException.startResolutionForResult(
                                            WeatherMapsActivity.this,
                                            CHECK_SETTINGS_CODE
                                    );
                                } catch (IntentSender.SendIntentException sie) {
                                    sie.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes
                                    .SETTINGS_CHANGE_UNAVAILABLE:
                                Toast.makeText(WeatherMapsActivity.this, getResources().getString(R.string.adjust_location_settings),
                                        Toast.LENGTH_LONG).show();
                                isLocationUpdatesActive = false;
                        }
                        updateLocationUi();
                    }
                });
    }

    private void stopLocationUpdates() {
        if (!isLocationUpdatesActive) {
            return;
        }
        fusedLocationClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        isLocationUpdatesActive = false;
                    }
                });
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                updateLocationUi();
            }
        };
    }

    private void updateLocationUi() {
        if (currentLocation != null) {
            LatLng userLocation = new LatLng(currentLocation.getLatitude(),
                    currentLocation.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            googleMap.addMarker(new MarkerOptions().position(userLocation).title(getResources().getString(R.string.your_location)));
            textLat.setText(String.valueOf(userLocation.latitude));
            textLon.setText(String.valueOf(userLocation.longitude));
        }
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLocationUpdatesActive && checkLocationPermission()) {
            startLocationUpdates();
        } else if (!checkLocationPermission()) {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length <= 0) {
                Log.d(TAG,
                        "Request was cancelled");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isLocationUpdatesActive) {
                    startLocationUpdates();
                }
            }
        }
    }

    private boolean checkLocationPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
}