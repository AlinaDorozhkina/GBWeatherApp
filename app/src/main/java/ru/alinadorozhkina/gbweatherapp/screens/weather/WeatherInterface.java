package ru.alinadorozhkina.gbweatherapp.screens.weather;

import java.util.List;

import ru.alinadorozhkina.gbweatherapp.parcelable.entities.CurrentWeather;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.WeekWeather;

public interface WeatherInterface {
    public void getCurrentWeather(CurrentWeather currentWeather);

    public void showError();

    public void getWeekWeatherList(List<WeekWeather> weekWeatherList);
}
