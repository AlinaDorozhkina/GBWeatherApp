package ru.alinadorozhkina.gbweatherapp.DB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This class holds the implementation code for the
 * methods that interact with the database.
 * Using a repository allows us to group the implementation
 * methods together, and allows the ViewModel to be
 * a clean interface between the rest of the app and the database.
 * <p>
 * For insert, update and delete, and longer-running queries,
 * you must run the database interaction methods in the background.
 * Typically, all you need to do to implement a database method
 * is to call it on the data access object (DAO),
 * in the background if applicable.
 */
public class Repository {
    private FavouritesDao dao;
    private LiveData<List<Favourites>> favouritesList;

    public Repository(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        dao = db.fDao();
        favouritesList = dao.getAll();
    }

    LiveData<List<Favourites>> getAllFavourites() {
        return favouritesList;
    }

    public void insert(Favourites favourites) {
        new InsertAsyncTask(dao).execute(favourites);
    }

    public void deleteAll() {
        new DeleteAllWordsAsyncTask(dao).execute();
    }

    public void deleteFavourites(Favourites favourites) {
        new DeleteWordAsyncTask(dao).execute(favourites);
    }

    public Favourites getFavouritesByName(String name) {
        try {
            return new GetFavouriteByNameTask(dao).execute(name).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Static inner classes below here to run database interactions
     * in the background.
     */
    private static class InsertAsyncTask extends AsyncTask<Favourites, Void, Void> {

        private FavouritesDao mAsyncTaskDao;

        InsertAsyncTask(FavouritesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Favourites... params) {
            mAsyncTaskDao.insertFavourites(params[0]);
            return null;
        }
    }

    /**
     * Delete all words from the database (does not delete the table)
     */
    private static class DeleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavouritesDao mAsyncTaskDao;

        DeleteAllWordsAsyncTask(FavouritesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     * Delete a single word from the database.
     */
    private static class DeleteWordAsyncTask extends AsyncTask<Favourites, Void, Void> {
        private FavouritesDao mAsyncTaskDao;

        DeleteWordAsyncTask(FavouritesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Favourites... params) {
            mAsyncTaskDao.deleteFavourites(params[0]);
            return null;
        }
    }

    private static class GetFavouriteByNameTask extends AsyncTask<String, Void, Favourites> {
        private FavouritesDao mAsyncTaskDao;

        GetFavouriteByNameTask(FavouritesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Favourites doInBackground(String... strings) {
            if (strings != null) {
                return mAsyncTaskDao.getFavouritesByName(strings[0]);
            }
            return null;
        }
    }
}
