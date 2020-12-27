package ru.alinadorozhkina.gbweatherapp.current.weather.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("country")
    @Expose
    String country;
    @SerializedName("coord")
    @Expose
    Coord coord;

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Coord getCoord() {
        return coord;
    }
}
