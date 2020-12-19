package ru.alinadorozhkina.gbweatherapp.data.base.favourites;

import android.os.Parcel;
import android.os.Parcelable;

public class FavouriteCity implements Parcelable {
    private String name;
    private String temperature;
    private String data;

    public FavouriteCity(String name, String temperature, String data) {
        this.name = name;
        this.temperature = temperature;
        this.data=data;
    }

    protected FavouriteCity(Parcel in) {
        name = in.readString();
        temperature = in.readString();
        data= in.readString();
    }

    public static final Creator<FavouriteCity> CREATOR = new Creator<FavouriteCity>() {
        @Override
        public FavouriteCity createFromParcel(Parcel in) {
            return new FavouriteCity(in);
        }

        @Override
        public FavouriteCity[] newArray(int size) {
            return new FavouriteCity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(temperature);
        dest.writeString(data);
    }

    @Override
    public String toString() {
        return "FavouriteCity{" +
                "name='" + name + '\'' +
                ", temperature=" + temperature +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getData() {
        return data;
    }
}

