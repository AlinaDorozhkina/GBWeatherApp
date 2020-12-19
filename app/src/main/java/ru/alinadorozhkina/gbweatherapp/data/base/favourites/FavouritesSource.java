package ru.alinadorozhkina.gbweatherapp.data.base.favourites;

//import androidx.lifecycle.LiveData;
//
//import java.util.List;
//
//public class FavouritesSource {
//    private final FavouritesDao favouritesDao;
//    private LiveData<List<Favourites>> favouritesList;
//
//    public FavouritesSource(FavouritesDao favouritesDao) {
//        this.favouritesDao = favouritesDao;
//    }
//
//    public LiveData<List<Favourites>> getFavouritesList() {
//        if (favouritesList==null){
//            favouritesList=favouritesDao.getAll();
//        }
//        return favouritesList;
//    }
//
//    public  void addFavourites(Favourites f){
//        favouritesDao.insertFavourites(f);
//        favouritesList=favouritesDao.getAll();
//    }
//
//    public void deleteById(int id){
//        favouritesDao.deleteById(id);
//        favouritesList=favouritesDao.getAll();
//    }
//
//    public int getCountFavourites(){
//        return favouritesDao.getCountFavourites();
//    }
//}
