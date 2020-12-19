package ru.alinadorozhkina.gbweatherapp.data.base.favourites;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface FavouritesDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertFavourites (Favourites favourites);
    @Update
    void updateFavourites (Favourites favourites);
    @Delete
    void deleteFavourites (Favourites favourites);
    @Query("SELECT * FROM favourites")
    LiveData<List<Favourites>> getAll();

    @Query("DELETE FROM favourites WHERE id = :id" )
    void deleteById (int id);

    @Query("DELETE FROM favourites")
    void deleteAll();

    @Query("SELECT COUNT() FROM favourites")
    int getCountFavourites();
}
