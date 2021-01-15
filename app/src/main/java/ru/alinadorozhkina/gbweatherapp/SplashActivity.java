package ru.alinadorozhkina.gbweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


import ru.alinadorozhkina.gbweatherapp.helper.Keys;
import ru.alinadorozhkina.gbweatherapp.screens.weather.WeatherDescription;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = this.getSharedPreferences(Keys.SHARED_NAME, MODE_PRIVATE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("location", false)) {
            Log.v(TAG, " sharedPreferences.contains(location))");
            Intent intent = new Intent(this, WeatherDescription.class);
            intent.putExtra(Keys.COORDINATES, true);
            startActivity(intent);
        } else if (sharedPreferences.contains(Keys.SAVE_CITY)) {
            Log.v(TAG, " sharedPreferences.contains(Keys.SAVE_CITY))");
            String city = sharedPreferences.getString(Keys.SAVE_CITY, null);
            if (city != null) {
                Intent intent = new Intent(this, WeatherDescription.class);
                intent.putExtra(Keys.CITY, city);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
