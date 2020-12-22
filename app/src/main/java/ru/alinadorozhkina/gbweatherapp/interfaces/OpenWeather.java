package ru.alinadorozhkina.gbweatherapp.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import ru.alinadorozhkina.gbweatherapp.current.weather.entities.WeatherRequest;


public interface OpenWeather {
    @GET("data/2.5/forecast")
    Call<WeatherRequest> loadWeather1(@Query("q") String cityCountry, @Query("lang") String language, @Query("units") String unitsValue, @Query("appid")String keyApi);
}


