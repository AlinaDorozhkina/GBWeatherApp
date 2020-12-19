package ru.alinadorozhkina.gbweatherapp.current.weather.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("weather")
    @Expose
    private Weather[] weather;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("dt_txt")
    @Expose
    private String dt_txt;

    public Main getMain() {
        return main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public String getDt_txt() {
        return dt_txt;
    }
}

