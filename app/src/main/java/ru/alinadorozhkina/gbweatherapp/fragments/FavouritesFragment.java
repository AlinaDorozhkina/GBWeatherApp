package ru.alinadorozhkina.gbweatherapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ru.alinadorozhkina.gbweatherapp.App;
import ru.alinadorozhkina.gbweatherapp.DB.FavViewModel;
import ru.alinadorozhkina.gbweatherapp.DB.Favourites;
import ru.alinadorozhkina.gbweatherapp.R;
import ru.alinadorozhkina.gbweatherapp.adapters.FavouritesAdapter;

public class FavouritesFragment extends Fragment {
    private FavViewModel viewModel;
    private FavouritesAdapter favouritesAdapter;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        FloatingActionButton fab_delete_all = view.findViewById(R.id.fab_delete_all);
        recyclerView = view.findViewById(R.id.recycleView_for_favourites_city);
        fab_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getAllFavourites().getValue().size() ==0) {
                    Toast.makeText(getContext(), " удалять нечего", Toast.LENGTH_LONG).show();
                } else {
                    viewModel.deleteAll();
                    Toast.makeText(getContext(), "все удалено", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void initViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance()).create(FavViewModel.class);
        viewModel.getAllFavourites().observe(getViewLifecycleOwner(), new Observer<List<Favourites>>() {
            @Override
            public void onChanged(List<Favourites> favourites) {
                if (favourites != null) {
                    favouritesAdapter.setFavourites(favourites);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecycleView();
        initViewModel();
        super.onViewCreated(view, savedInstanceState);

    }

    private void initRecycleView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        favouritesAdapter = new FavouritesAdapter(App.getInstance());
        recyclerView.setAdapter(favouritesAdapter);
    }


}