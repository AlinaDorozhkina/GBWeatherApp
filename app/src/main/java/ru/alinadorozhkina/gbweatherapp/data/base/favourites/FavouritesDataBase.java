package ru.alinadorozhkina.gbweatherapp.data.base.favourites;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.alinadorozhkina.gbweatherapp.App;

@Database(entities = {Favourites.class}, version = 1 , exportSchema = false)
public abstract class FavouritesDataBase extends RoomDatabase {
    private static final String DB_NAME = "favouritesCities.db";

    public abstract FavouritesDao getFavouritesDao();

    public synchronized static FavouritesDataBase createDB() {
        return Room.databaseBuilder(App.getInstance(), FavouritesDataBase.class, DB_NAME).build();
    }
}
