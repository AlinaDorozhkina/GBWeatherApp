package ru.alinadorozhkina.gbweatherapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.alinadorozhkina.gbweatherapp.App;
import ru.alinadorozhkina.gbweatherapp.R;
import ru.alinadorozhkina.gbweatherapp.adapters.FavouritesAdapter;
import ru.alinadorozhkina.gbweatherapp.data.base.favourites.FavouriteCity;
import ru.alinadorozhkina.gbweatherapp.data.base.favourites.Favourites;

import ru.alinadorozhkina.gbweatherapp.data.base.favourites.FavouritesViewModel;


public class FavouritesFragment extends Fragment {
    private static final String TAG = FavouritesFragment.class.getSimpleName();
        private FavouritesAdapter favouritesAdapter;
        private RecyclerView recyclerView;
        private FavouritesViewModel viewModel;
    private ArrayList<Favourites> favourites= new ArrayList<>();

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.layout_for_favourites, container, false);
            recyclerView = view.findViewById(R.id.recycleView_for_favourites_city);
            initRecycleView();
            return view;
        }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel= new ViewModelProvider(getActivity()).get(FavouritesViewModel.class);
    }

    public void initRecycleView (){
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
//            FavouritesDao favouritesDao= App.getInstance().getFavouritesDao();
//            favouritesSource=new FavouritesSource(favouritesDao);
            favouritesAdapter =new FavouritesAdapter(favourites);
            getData();
            recyclerView.setAdapter(favouritesAdapter);
        }

    private void getData () {
        LiveData<List<Favourites>> favouritesFromDB = viewModel.getFavourites();
        Log.v(TAG, " getData"+ favouritesFromDB.toString());
        favouritesFromDB.observe(this, new Observer<List<Favourites>>() {
            @Override
            public void onChanged(List<Favourites> favouritesFromLiveData) {
                favouritesAdapter.setFavourites(favouritesFromLiveData);
//                favourites.clear();
//                favourites.addAll(favouritesFromLiveData);
//                favouritesAdapter.notifyDataSetChanged();
            }
        });
//            favourites.addAll(favouritesFromDB);
//            prepareFavourites();

    }

//        public FavouritesAdapter setFavoriteCities(ArrayList<Favourites> cities) {
//            recyclerView.setHasFixedSize(true);
//            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//            recyclerView.setLayoutManager(linearLayoutManager);
//            favouritesAdapter =new FavouritesAdapter(cities, getActivity());
//            recyclerView.setAdapter(favouritesAdapter);
//            return favouritesAdapter;
//        }

        public FavouritesAdapter getFavouritesAdapter() {
            return favouritesAdapter;
        }

}
