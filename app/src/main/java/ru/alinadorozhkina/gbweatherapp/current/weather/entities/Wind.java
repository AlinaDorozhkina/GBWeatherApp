package ru.alinadorozhkina.gbweatherapp.current.weather.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    @Expose
    private double speed;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
