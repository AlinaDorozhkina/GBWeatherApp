package ru.alinadorozhkina.gbweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.alinadorozhkina.gbweatherapp.DB.Favourites;
import ru.alinadorozhkina.gbweatherapp.adapters.WeekTempAdapter;
import ru.alinadorozhkina.gbweatherapp.current.weather.entities.WeatherRequest;

import ru.alinadorozhkina.gbweatherapp.fragments.CurrentWeatherFragment;
import ru.alinadorozhkina.gbweatherapp.helper.Keys;
import ru.alinadorozhkina.gbweatherapp.interfaces.OpenWeather;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.CurrentWeather;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.WeekWeather;

public class WeatherDescription extends AppCompatActivity  {

    public static final String BROADCAST_ACTION_FINISHED = "service get result";
    private static final String TAG = WeatherDescription.class.getSimpleName();
    private String city;
    private CurrentWeather currentWeather;
    private OpenWeather openWeather;
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_description);
        //viewModel=new ViewModelProvider(this).get(FavouritesViewModel.class);
        if (getIntent().hasExtra(Keys.CITY)) {
            city = getIntent().getStringExtra(Keys.CITY);
            Log.v(TAG, " получен интент " + city);
        }
        initRetrofit();
        requestRetrofit(city, "ru", "metric", BuildConfig.WEATHER_API_KEY);


// использовала до Retrofit
//        URL url = NetworkUtils.buildURL(city);
//        Log.v(TAG, url.toString());
//
//        Intent intentMyIntentService = new Intent(this, MyIntentService.class);
//        startService(intentMyIntentService.putExtra(Keys.URL, url.toString()));
//        DownloadWeatherTask task = new DownloadWeatherTask();
//        task.execute(String.format(WEATHER_URL, city, BuildConfig.WEATHER_API_KEY));
    }

    private void initRetrofit(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);

    }
    private void requestRetrofit (String city, String language, String unitsValue, String api_key){
        openWeather.loadWeather(city, language,unitsValue, api_key).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response.body()!=null){
                    String cityName = response.body().getCity().getName();
                    Log.v(TAG, " название города "+cityName );
                    initRecycleView(getWeekWeather(response));
                    int temp =(int) response.body().getList()[0].getMain().getTemp();
                    String description = response.body().getList()[0].getWeather()[0].getDescription();
                    String icon = response.body().getList()[0].getWeather()[0].getIcon();
                    int pressure = response.body().getList()[0].getMain().getPressure();
                    int wind = (int) response.body().getList()[0].getWind().getSpeed();
                    currentWeather = new CurrentWeather(cityName, temp, description, icon, wind, pressure);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Keys.CURRENT_WEATHER, currentWeather);
                    CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.init(currentWeather);
                    currentWeatherFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_current_weather_fragment, currentWeatherFragment).commit();
                   // initRecycleView(getWeekWeather(response));
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherDescription.this);
                builder.setTitle(R.string.error)
                        .setMessage(R.string.city_error)
                        .setIcon(ContextCompat.getDrawable(WeatherDescription.this, R.drawable.ic_baseline_error_24))
                        .setCancelable(false)
                        .setPositiveButton(R.string.button_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }
    private ArrayList<WeekWeather> getWeekWeather(Response<WeatherRequest> response){
        String data1 = "";
        ArrayList<WeekWeather> weekWeathersList = new ArrayList<>();
        for (int i = 0; i < response.body().getList().length; i++) {
            WeekWeather current = new WeekWeather();
            String text = response.body().getList()[i].getDt_txt();
            String data = editDay(text);
            if (data.equals(data1)) {
            } else {
                current.setDay(data);
                data1 = data;
                current.setTemp(response.body().getList()[i].getMain().getTemp());
                current.setIcon(response.body().getList()[i].getWeather()[0].getIcon());
                weekWeathersList.add(current);
                Log.v(TAG, weekWeathersList.toString());
            }
        }
        return weekWeathersList;
    }

    private static String editDay(String data) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat fmt2 = new SimpleDateFormat("E, d MMM", Locale.getDefault());
        try {
            Date date = fmt.parse(data);
            return fmt2.format(date);
        } catch (ParseException pe) {
            return "Date";
        }
    }

    private WeekTempAdapter initRecycleView(ArrayList<WeekWeather> weatherList) {
        RecyclerView recyclerView = findViewById(R.id.recycleView_for_week_weather);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        WeekTempAdapter weekTempAdapter = new WeekTempAdapter(this, weatherList);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(weekTempAdapter);
        return weekTempAdapter;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        registerReceiver(JsonResultReceiver, new IntentFilter(BROADCAST_ACTION_FINISHED));
//    }
//
//    private BroadcastReceiver JsonResultReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // нужно для работы сервиса
//            if (intent.hasExtra(Keys.CURRENT_WEATHER)) {
//                currentWeather = intent.getParcelableExtra(Keys.CURRENT_WEATHER);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(Keys.CURRENT_WEATHER, currentWeather);
//                CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.init(currentWeather);
//                currentWeatherFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_current_weather_fragment, currentWeatherFragment).commit();
//            }
//            if (intent.hasExtra(Keys.JSON_RESULT)) {
//                initRecycleView(intent.getParcelableArrayListExtra(Keys.JSON_RESULT));
//            }
//        }
//    };
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterReceiver(JsonResultReceiver);
//   }


}