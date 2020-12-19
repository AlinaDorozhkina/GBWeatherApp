package ru.alinadorozhkina.gbweatherapp;

import android.app.Application;

import ru.alinadorozhkina.gbweatherapp.data.base.favourites.FavouritesDao;
import ru.alinadorozhkina.gbweatherapp.data.base.favourites.FavouritesDataBase;

public class App extends Application {

    private static App instance;
    private FavouritesDataBase db;

    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        db = FavouritesDataBase.createDB();
    }
    public FavouritesDao getFavouritesDao (){
        return db.getFavouritesDao();
    }
}
