package ru.alinadorozhkina.gbweatherapp.data.base.favourites;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "favourites",indices = @Index(value = {Favourites.CITY_NAME}))
public class Favourites {
    public final static String ID = "id";
    public final static String CITY_NAME = "city_name";
    public final static String TEMPERATURE = "temperature";
    public final static String DATA = "data";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name =  ID)
    private int id;
    @ColumnInfo(name =  CITY_NAME)
    private String cityName;
    @ColumnInfo(name = TEMPERATURE)
    private String temperature;
    @ColumnInfo(name =DATA)
    private String data;

    public Favourites(int id, String cityName, String temperature, String data) {
        this.id = id;
        this.cityName = cityName;
        this.temperature = temperature;
        this.data = data;
    }

    @Ignore
    public Favourites(String cityName, String temperature, String data) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
