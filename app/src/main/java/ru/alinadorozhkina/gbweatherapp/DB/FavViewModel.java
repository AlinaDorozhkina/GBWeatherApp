package ru.alinadorozhkina.gbweatherapp.DB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * The ViewModel provides the interface between the UI and the data layer of the app,
 * represented by the repository
 */

public class FavViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Favourites>> favouritesList;


    public FavViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        favouritesList = repository.getAllFavourites();
    }

    public LiveData<List<Favourites>> getAllFavourites() {
        return favouritesList;
    }

    public void insert(Favourites f) {
        repository.insert(f);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteFavourites(Favourites f) {
        repository.deleteFavourites(f);
    }

    public Favourites getFavouritesByName(String name) {
        return repository.getFavouritesByName(name);
    }
}
