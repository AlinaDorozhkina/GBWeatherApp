package ru.alinadorozhkina.gbweatherapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.alinadorozhkina.gbweatherapp.DB.Favourites;
import ru.alinadorozhkina.gbweatherapp.R;
import ru.alinadorozhkina.gbweatherapp.WeatherDescription;
import ru.alinadorozhkina.gbweatherapp.helper.Keys;


public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {
    private final String TAG = FavouritesAdapter.class.getSimpleName();
    List<Favourites> favouritesList;
    private Context context;

    public FavouritesAdapter(Context context) {
        this.context = context;
        favouritesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_cities, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, final int position) {
        Favourites favourites = favouritesList.get(position);
        holder.textView_favourite_city.setText(favourites.getCityName());
        holder.textView_favourite_city_temp.setText(favourites.getTemperature());
        holder.textView_favourite_city_data.setText(favourites.getData());
    }

    @Override
    public int getItemCount() {
        Log.v(TAG, " getItemCount() " + favouritesList.size());
        return favouritesList.size();
    }

    public void setFavourites(List<Favourites> favourites) {
        Log.v(TAG, " setFavourites " + favourites.size());
        this.favouritesList = favourites;
        notifyDataSetChanged();
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView_favourite_city;
        private TextView textView_favourite_city_temp;
        private TextView textView_favourite_city_data;

        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView_favourite_city = itemView.findViewById(R.id.textView_favourite_city);
            textView_favourite_city_temp = itemView.findViewById(R.id.textView_favourite_city_temp);
            textView_favourite_city_data = itemView.findViewById(R.id.textView_favourite_city_data);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String value = favouritesList.get(position).getCityName();
            Intent intent = new Intent(context, WeatherDescription.class);
            intent.putExtra(Keys.CITY, value);
            context.startActivity(intent);
        }
    }
}
