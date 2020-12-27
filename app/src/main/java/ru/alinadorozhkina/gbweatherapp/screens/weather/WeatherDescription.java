package ru.alinadorozhkina.gbweatherapp.screens.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ru.alinadorozhkina.gbweatherapp.MainActivity;
import ru.alinadorozhkina.gbweatherapp.R;
import ru.alinadorozhkina.gbweatherapp.SplashActivity;
import ru.alinadorozhkina.gbweatherapp.adapters.WeekTempAdapter;
import ru.alinadorozhkina.gbweatherapp.fragments.CurrentWeatherFragment;
import ru.alinadorozhkina.gbweatherapp.helper.Keys;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.CurrentWeather;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.WeekWeather;

public class WeatherDescription extends AppCompatActivity implements WeatherInterface {

    public static final String BROADCAST_ACTION_FINISHED = "service get result";
    private static final String TAG = WeatherDescription.class.getSimpleName();
    private String city;
    private SharedPreferences sharedPreferences;
    private WeatherPresenter presenter;
    private static final int PERMISSION_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("theme", true)) {
            setTheme(R.style.AppDarkTheme);
        }
        setContentView(R.layout.activity_weather_description);
        initToolBar();
        initIntent();
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbarWeatherDescr);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_home_24);
        setSupportActionBar(toolbar);
    }

    private void initIntent() {
        if (getIntent().hasExtra(Keys.CITY)) {
            city = getIntent().getStringExtra(Keys.CITY);
            sharedPreferences = this.getSharedPreferences(Keys.SHARED_NAME, MODE_PRIVATE);
            saveCity(city);
            Log.v(TAG, " получен интент " + city);
            presenter = new WeatherPresenter(this);
            presenter.loadDataByName(city);
        } else if (getIntent().hasExtra(Keys.COORDINATES)) {
            requestPemissions();
        } else if (getIntent().hasExtra(Keys.LAT) && getIntent().hasExtra(Keys.LON)) {
            presenter = new WeatherPresenter(this);
            presenter.loadDataByCoord(getIntent().getDoubleExtra(Keys.LAT, 0), getIntent().getDoubleExtra(Keys.LON, 0));
        } else {
            startActivity(new Intent(WeatherDescription.this, MainActivity.class));
        }
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

    private void initRecycleView(List<WeekWeather> weatherList) {
        Log.v(TAG, "initRecycleView");
        RecyclerView recyclerView = findViewById(R.id.recycleView_for_week_weather);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        WeekTempAdapter weekTempAdapter = new WeekTempAdapter(this, weatherList);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(weekTempAdapter);
    }

    private void saveCity(String city) {
        sharedPreferences = this.getSharedPreferences("shared_last_city", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Keys.SAVE_CITY, city);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public void getCurrentWeather(CurrentWeather currentWeather) {
        Log.v(TAG, "поток " + Thread.currentThread().toString());
        Bundle bundle = new Bundle();
        bundle.putParcelable(Keys.CURRENT_WEATHER, currentWeather);
        CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.init(currentWeather);
        currentWeatherFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_current_weather_fragment, currentWeatherFragment).commit();
    }

    @Override
    public void showError() {
        Log.v(TAG, "поток " + Thread.currentThread().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherDescription.this);
        builder.setTitle(R.string.error)
                .setMessage(R.string.city_error)
                .setIcon(ContextCompat.getDrawable(WeatherDescription.this, R.drawable.ic_baseline_error_24))
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void getWeekWeatherList(List<WeekWeather> weekWeatherList) {
        Log.v(TAG, "поток " + Thread.currentThread().toString());
        initRecycleView(weekWeatherList);
    }


    private void requestPemissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.v(TAG, " получены кординаты " + location.getLatitude() + " " + location.getLongitude());
                    presenter = new WeatherPresenter(WeatherDescription.this);
                    presenter.loadDataByCoord(location.getLatitude(), location.getLongitude());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }
}