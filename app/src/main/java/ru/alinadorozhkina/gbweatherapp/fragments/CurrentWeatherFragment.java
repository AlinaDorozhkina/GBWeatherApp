package ru.alinadorozhkina.gbweatherapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.alinadorozhkina.gbweatherapp.DB.Favourites;
import ru.alinadorozhkina.gbweatherapp.adapters.WeekTempAdapter;
import ru.alinadorozhkina.gbweatherapp.helper.Keys;
import ru.alinadorozhkina.gbweatherapp.R;
import ru.alinadorozhkina.gbweatherapp.interfaces.OnActivityFavouritesAddingListener;
import ru.alinadorozhkina.gbweatherapp.interfaces.OnFragmentFavouritesListener;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.CurrentWeather;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.WeekWeather;


public class CurrentWeatherFragment extends Fragment implements OnActivityFavouritesAddingListener {
    private final String TAG = CurrentWeatherFragment.class.getSimpleName();
    private TextView textViewTemperature;
    private TextView textViewCity;
    private TextView textViewDescription;
    private ImageView imageViewWeatherIcon;
    private Context context;
    private String city;
    private CurrentWeather currentWeather;
    private String imageUrl = "http://openweathermap.org/img/wn/%s@2x.png";
    private TextView textViewPressureValue;
    private TextView textViewWindSpeedValue;
    private TextView textViewData;
    private TextView textViewHumidityValue;
    private double lat;
    private double lon;
    private View layout;
    private OnFragmentFavouritesListener listener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.layout_weather_current, container, false);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        textViewCity = layout.findViewById(R.id.textViewCity);
        textViewTemperature = layout.findViewById(R.id.textViewTemperature);
        textViewDescription = layout.findViewById(R.id.textViewDescription);
        textViewPressureValue = layout.findViewById(R.id.textViewPressureValue);
        textViewWindSpeedValue = layout.findViewById(R.id.textViewWindSpeedValue);
        textViewHumidityValue = layout.findViewById(R.id.textViewHumidityValue);
        imageViewWeatherIcon = layout.findViewById(R.id.imageViewWeatherIcon);
        textViewData = layout.findViewById(R.id.textViewData);
        textViewData.setText(getTodayDateInStringFormat());
        context = getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentFavouritesListener) {
            listener = (OnFragmentFavouritesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragment1DataListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            currentWeather = args.getParcelable(Keys.CURRENT_WEATHER);
            lat=currentWeather.getLat();
            lon=currentWeather.getLon();
            city = currentWeather.getCityName();
            textViewCity.setText(currentWeather.getCityName());
            textViewTemperature.setText(String.format("%s °", currentWeather.getTemperature()));
            textViewDescription.setText(currentWeather.getDescription());
            String icon = currentWeather.getIcon();
            Picasso.with(getContext()).load(String.format(imageUrl, icon)).into(imageViewWeatherIcon);
            textViewPressureValue.setText(String.format("%s мм.рт.ст", currentWeather.getPressure()));
            textViewWindSpeedValue.setText(String.format("%s м/с", currentWeather.getWind()));
            textViewHumidityValue.setText(String.format("%s процентов", currentWeather.getHumidity()));
            initRecycleView(currentWeather.getWeekWeathersList());
        }
    }

    public static CurrentWeatherFragment init(CurrentWeather currentWeather) {
        CurrentWeatherFragment currentWeatherFragment = new CurrentWeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Keys.CURRENT_WEATHER, currentWeather);
        currentWeatherFragment.setArguments(bundle);
        return currentWeatherFragment;
    }

    private String getTodayDateInStringFormat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
        return df.format(c.getTime());
    }

    private void initRecycleView(List<WeekWeather> weatherList) {
        Log.v(TAG, "initRecycleView");
        RecyclerView recyclerView = layout.findViewById(R.id.recycleView_for_week_weather);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        WeekTempAdapter weekTempAdapter = new WeekTempAdapter(context, weatherList);
        //DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        //itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        //recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(weekTempAdapter);
    }

    @Override
    public void updateFavourites() {
        Favourites fav = new Favourites(city, textViewTemperature.getText().toString(), textViewData.getText().toString(), lat, lon);
        Log.v(TAG, " вызван метод updateFavourites() , передано " + fav.getCityName());
        listener.sendDataToActivity(fav);
    }
}