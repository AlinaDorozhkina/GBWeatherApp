package ru.alinadorozhkina.gbweatherapp.parcelable.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CurrentWeather implements Parcelable {
    String cityName;
    double temperature;
    String description;
    String icon;
    int wind;
    int pressure;
    int humidity;
    double lat;
    double lon;
    ArrayList<WeekWeather> weekWeathersList;

    public CurrentWeather(String cityName, double temperature, String description, String icon, int wind, int pressure, int humidity, double lat, double lon, ArrayList<WeekWeather> weekWeathersList) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
        this.wind=wind;
        this.pressure=pressure;
        this.humidity=humidity;
        this.lat=lat;
        this.lon=lon;
        this.weekWeathersList=weekWeathersList;
    }

    protected CurrentWeather(Parcel in) {
        cityName=in.readString();
        temperature=in.readDouble();
        description=in.readString();
        icon=in.readString();
        wind=in.readInt();
        pressure=in.readInt();
        humidity=in.readInt();
        lat =in.readDouble();
        lon=in.readDouble();
        weekWeathersList=in.readArrayList(null);
    }

    public static final Creator<CurrentWeather> CREATOR = new Creator<CurrentWeather>() {
        @Override
        public CurrentWeather createFromParcel(Parcel in) {
            return new CurrentWeather(in);
        }

        @Override
        public CurrentWeather[] newArray(int size) {
            return new CurrentWeather[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeDouble(temperature);
        dest.writeString(description);
        dest.writeString(icon);
        dest.writeInt(wind);
        dest.writeInt(pressure);
        dest.writeInt(humidity);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeList(weekWeathersList);
    }

    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public int getWind() {
        return wind;
    }

    public int getPressure() {
        return pressure;
    }


    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "cityName='" + cityName + '\'' +
                ", temperature=" + temperature +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", wind=" + wind +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    public ArrayList<WeekWeather> getWeekWeathersList() {
        return weekWeathersList;
    }

    public void setWeekWeathersList(ArrayList<WeekWeather> weekWeathersList) {
        this.weekWeathersList = weekWeathersList;
    }
}