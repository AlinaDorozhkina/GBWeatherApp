package ru.alinadorozhkina.gbweatherapp.current.weather.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName("lat")
    @Expose
    double lat;
    @SerializedName("lon")
    @Expose
    double lon;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
