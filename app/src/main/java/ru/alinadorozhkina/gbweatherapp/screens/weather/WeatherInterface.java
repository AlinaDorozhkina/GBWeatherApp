package ru.alinadorozhkina.gbweatherapp.screens.weather;

import ru.alinadorozhkina.gbweatherapp.parcelable.entities.CurrentWeather;

public interface WeatherInterface {
    void getCurrentWeather(CurrentWeather currentWeather);

     void showError();

}
