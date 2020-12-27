package ru.alinadorozhkina.gbweatherapp.DB;

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
    public final static String LAT = "latitude";
    public final static String LON = "longitude";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name =  ID)
    private int id;
    @ColumnInfo(name =  CITY_NAME)
    private String cityName;
    @ColumnInfo(name = TEMPERATURE)
    private String temperature;
    @ColumnInfo(name =DATA)
    private String data;
    @ColumnInfo(name = LAT)
    private double lat;
    @ColumnInfo(name = LON)
    private double lon;


    public Favourites(int id, String cityName, String temperature, String data, double lat, double lon) {
        this.id = id;
        this.cityName = cityName;
        this.temperature = temperature;
        this.data = data;
        this.lat=lat;
        this.lon=lon;
    }

    @Ignore
    public Favourites(String cityName, String temperature, String data,double lat, double lon) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.data = data;
        this.lat=lat;
        this.lon=lon;
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
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
