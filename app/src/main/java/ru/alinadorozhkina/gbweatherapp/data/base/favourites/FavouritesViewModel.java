package ru.alinadorozhkina.gbweatherapp.data.base.favourites;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FavouritesViewModel extends ViewModel {
    private  FavouritesDataBase dataBase;
    private LiveData<List<Favourites>> favourites;

    public FavouritesViewModel (){
        dataBase=FavouritesDataBase.createDB();
        favourites=dataBase.getFavouritesDao().getAll();
    }

    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
    }

    public  void insertFavourites(Favourites f){
        new InsertTask().execute(f);
    }

    public void deleteFavourites(Favourites f){
        new DeleteTask().execute(f);
    }

    public void deleteAll(){
        new DeleteAllTask().execute();
    }
    private  class InsertTask extends AsyncTask<Favourites, Void, Void>{

        @Override
        protected Void doInBackground(Favourites... favourites) {
            if(favourites!=null & favourites.length>0){
                dataBase.getFavouritesDao().insertFavourites(favourites[0]);
            }
            return null;
        }
    }

    private  class DeleteTask extends AsyncTask<Favourites, Void, Void>{
        @Override
        protected Void doInBackground(Favourites... favourites) {
            if(favourites!=null & favourites.length>0){
                dataBase.getFavouritesDao().deleteFavourites(favourites[0]);
            }
            return null;
        }
    }

    private  class DeleteAllTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            dataBase.getFavouritesDao().deleteAll();
            return null;
        }
    }
}
