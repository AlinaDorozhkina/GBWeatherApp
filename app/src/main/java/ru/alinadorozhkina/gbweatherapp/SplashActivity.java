package ru.alinadorozhkina.gbweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import ru.alinadorozhkina.gbweatherapp.helper.Keys;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=this.getSharedPreferences(Keys.SHARED_NAME, MODE_PRIVATE);
        if (sharedPreferences!=null){
            String city = sharedPreferences.getString(Keys.SAVE_CITY, null);
            if (city!=null){
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