package ru.alinadorozhkina.gbweatherapp.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) for a favourites.
 * Each method performs a database operation, such as inserting or deleting a city,
 * running a DB query, or deleting all words.
 */
@Dao
public interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavourites(Favourites f);

    @Update
    void updateFavourites(Favourites f);

    @Delete
    void deleteFavourites(Favourites f);

    @Query("SELECT * FROM favourites")
    LiveData<List<Favourites>> getAll();

    @Query("DELETE FROM favourites")
    void deleteAll();

    @Query("SELECT * FROM favourites WHERE city_name ==:name")
    Favourites getFavouritesByName(String name);
}
