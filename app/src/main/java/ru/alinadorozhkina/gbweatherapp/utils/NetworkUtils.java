package ru.alinadorozhkina.gbweatherapp.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

import ru.alinadorozhkina.gbweatherapp.BuildConfig;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private static final String CITY = "q";

    private static final String PARAM_API_Key = "appid";
    private static final String PARAM_LANGUAGE = "lang";
    private static final String PARAM_UNITS = "units";

    private static final String API_CODE = BuildConfig.WEATHER_API_KEY;
    private static final String LANGUAGE_RUS = "ru";
    private static final String LANGUAGE_EN = "en"; // TODO в настройках сделать смену языка
    private static final String UNITS_VALUE_Celsius = "metric";
    private static final String UNITS_VALUE_Kelvin = null; // TODO в настройках сделать смену значений

    public static URL buildURL(String city) {
        URL result = null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(CITY, city)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE_RUS)
                .appendQueryParameter(PARAM_UNITS, UNITS_VALUE_Celsius)
                .appendQueryParameter(PARAM_API_Key, API_CODE)
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;

    }
}
