package ru.alinadorozhkina.gbweatherapp.screens.weather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import ru.alinadorozhkina.gbweatherapp.App;
import ru.alinadorozhkina.gbweatherapp.DB.FavViewModel;
import ru.alinadorozhkina.gbweatherapp.DB.Favourites;
import ru.alinadorozhkina.gbweatherapp.MainActivity;
import ru.alinadorozhkina.gbweatherapp.R;
import ru.alinadorozhkina.gbweatherapp.WeatherMapsActivity;
import ru.alinadorozhkina.gbweatherapp.fragments.CurrentWeatherFragment;
import ru.alinadorozhkina.gbweatherapp.fragments.FavouritesFragment;
import ru.alinadorozhkina.gbweatherapp.helper.Keys;
import ru.alinadorozhkina.gbweatherapp.interfaces.OnActivityFavouritesAddingListener;
import ru.alinadorozhkina.gbweatherapp.interfaces.OnFragmentFavouritesListener;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.CurrentWeather;


public class WeatherDescription extends AppCompatActivity implements OnFragmentFavouritesListener, WeatherInterface {

    public static final String BROADCAST_ACTION_FINISHED = "service get result";
    private static final String TAG = ru.alinadorozhkina.gbweatherapp.screens.weather.WeatherDescription.class.getSimpleName();
    private String city;
    private SharedPreferences sharedPreferences;
    private WeatherPresenter presenter;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private FavViewModel viewModel;
    private Favourites favourite_city;
    private FloatingActionButton favouritesButton;
    private OnActivityFavouritesAddingListener addingListener;
    private View coordinatorLayoutView;
    private FavouritesFragment favFra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(Keys.THEME, true)) {
            setTheme(R.style.AppDarkTheme);
        }
        setContentView(R.layout.layout_weather_description);
        initIntent();
        initToolBar();
        favouritesButton = findViewById(R.id.add_button);
        favouritesButton.setOnClickListener(clickListener);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance()).create(FavViewModel.class);
        coordinatorLayoutView = findViewById(R.id.coordinator_main);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, " вызван метод updateFavourites()");
            addingListener.updateFavourites();
            Log.v(TAG, " вызван метод updateFavourites()");

        }
    };

    private void setFavourites() {
        favourite_city = viewModel.getFavouritesByName(city);
        if (favourite_city != null) {
            Log.v(TAG, favourite_city.getCityName());
            favouritesButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_horizontal_rule_24));
        } else {
            Log.v(TAG, "setFavourites()  favourite_city = null");
            favouritesButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_add_24));
        }
    }

    private void initToolBar() {
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);
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
            requestPermissions();
        } else if (getIntent().hasExtra(Keys.LAT) && getIntent().hasExtra(Keys.LON)) {
            presenter = new WeatherPresenter(this);
            presenter.loadDataByCoord(getIntent().getDoubleExtra(Keys.LAT, 0), getIntent().getDoubleExtra(Keys.LON, 0));
        } else {
            startActivity(new Intent(WeatherDescription.this, MainActivity.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(WeatherDescription.this, MainActivity.class));
                break;
            case R.id.favourites:
                if (favFra == null) {
                    favFra = new FavouritesFragment();
                    getSupportFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_bottom, R.anim.slide_top)
                            .add(R.id.frame_for_favourites, favFra)
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .remove(favFra)
                            .commit();
                    favFra = null;
                }
                break;
            case R.id.map:
                startActivity(new Intent (WeatherDescription.this, WeatherMapsActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar, menu);
        return true;
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
        if (currentWeather == null) {
            Log.v(TAG, "  ошибка current weather == null ");
        } else {
            Log.v(TAG, "currentWeather.getCityName() " + currentWeather.getCityName());
            city = currentWeather.getCityName();
            setFavourites();
            Log.v(TAG, "поток " + Thread.currentThread().toString());
            Bundle bundle = new Bundle();
            bundle.putParcelable(Keys.CURRENT_WEATHER, currentWeather);
            CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.init(currentWeather);
            addingListener = (OnActivityFavouritesAddingListener) currentWeatherFragment;
            currentWeatherFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_weather, currentWeatherFragment).commit();
        }
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
                                Intent intent = new Intent(WeatherDescription.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void sendDataToActivity(Favourites favouriteCity) {
        Log.v(TAG, " метод sendDataToActivity " + favouriteCity.getCityName());
        if (favourite_city == null) {
            viewModel.insert(favouriteCity);
            Log.v(TAG, "viewModel.insert(favouriteCity) " + favouriteCity.getCityName());
            Snackbar
                    .make(coordinatorLayoutView, getString(R.string.snackbar_message_add, city), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(this, R.color.grey))
                    .setAnchorView(favouritesButton)
                    .setAction("Action", null).show();
        } else {
            viewModel.deleteFavourites(favourite_city);
            Log.v(TAG, "viewModel.deleteFavourites(favourite_city)" + favouriteCity.getCityName());
            Snackbar
                    .make(coordinatorLayoutView, getString(R.string.snackbar_message_delete, city), Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(this, R.color.grey))
                    .setAnchorView(favouritesButton)
                    .setAction("Action", null).show();
        }
        setFavourites();
    }


    private void requestPermissions() {
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

