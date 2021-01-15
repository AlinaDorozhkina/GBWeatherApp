package ru.alinadorozhkina.gbweatherapp.interfaces;

import ru.alinadorozhkina.gbweatherapp.DB.Favourites;

public interface OnFragmentFavouritesListener {
    void sendDataToActivity (Favourites favouriteCity);
}
