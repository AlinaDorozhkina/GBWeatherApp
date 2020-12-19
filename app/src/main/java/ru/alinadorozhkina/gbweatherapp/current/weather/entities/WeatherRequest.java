package ru.alinadorozhkina.gbweatherapp.current.weather.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherRequest {
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("list")
    @Expose
    private List[] list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List[] getList() {
        return list;
    }

    public void setList(List[] list) {
        this.list = list;
    }
}
