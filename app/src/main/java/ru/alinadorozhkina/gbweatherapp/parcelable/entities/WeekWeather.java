package ru.alinadorozhkina.gbweatherapp.parcelable.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class WeekWeather implements Parcelable {

    private String day;
    private double temp;
    private String icon;

    public WeekWeather(){

    }

    public WeekWeather(String day, double temp, String iconUrl) {
        this.day = day;
        this.temp = temp;
        this.icon = iconUrl;
    }

    protected WeekWeather(Parcel in) {
        day = in.readString();
        temp = in.readDouble();
        icon = in.readString();
    }

    public static final Creator<WeekWeather> CREATOR = new Creator<WeekWeather>() {
        @Override
        public WeekWeather createFromParcel(Parcel in) {
            return new WeekWeather(in);
        }

        @Override
        public WeekWeather[] newArray(int size) {
            return new WeekWeather[size];
        }
    };

    public String getDay() {
        return day;
    }

    public double getTemp() {
        return temp;
    }

    public String getIconUrl() {
        return icon;
    }

    public void setDay(String day) {

        this.day = day;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeDouble(temp);
        dest.writeString(icon);
    }

    @Override
    public String toString() {
        return "WeekWeather{" +
                "day='" + day + '\'' +
                ", temp=" + temp +
                ", icon='" + icon + '\'' +
                '}';
    }
}
