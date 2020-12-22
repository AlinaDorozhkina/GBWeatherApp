package ru.alinadorozhkina.gbweatherapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ru.alinadorozhkina.gbweatherapp.screens.weather.WeatherDescription;
import ru.alinadorozhkina.gbweatherapp.current.weather.entities.WeatherRequest;
import ru.alinadorozhkina.gbweatherapp.helper.Keys;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.CurrentWeather;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.WeekWeather;
import ru.alinadorozhkina.gbweatherapp.utils.JSONUtils;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection urlConnection = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(intent.getStringExtra(Keys.URL));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            Log.v("MyintentService", sb.toString());
            initJsonModel(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private void initJsonModel(String result) {
        if (result != null) {
            Gson gson = new Gson();
            WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
            CurrentWeather currentWeather = JSONUtils.initCurrentWeather(weatherRequest);
            ArrayList<WeekWeather> weekWeathersList = JSONUtils.initWeekWeatherList(weatherRequest);
            sendBroadcast(currentWeather, weekWeathersList);
        }
    }

    private void sendBroadcast(CurrentWeather currentWeather, ArrayList<WeekWeather> weekWeathersList) {
        Intent broadcastIntent = new Intent(WeatherDescription.BROADCAST_ACTION_FINISHED);
        broadcastIntent.putExtra(Keys.CURRENT_WEATHER, currentWeather);
        broadcastIntent.putParcelableArrayListExtra(Keys.JSON_RESULT, weekWeathersList);
        sendBroadcast(broadcastIntent);
    }


}