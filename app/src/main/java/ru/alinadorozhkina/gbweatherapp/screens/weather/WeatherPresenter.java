package ru.alinadorozhkina.gbweatherapp.screens.weather;


import android.os.AsyncTask;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.alinadorozhkina.gbweatherapp.BuildConfig;
import ru.alinadorozhkina.gbweatherapp.api.ApiFactory;
import ru.alinadorozhkina.gbweatherapp.current.weather.entities.WeatherRequest;
import ru.alinadorozhkina.gbweatherapp.interfaces.OpenWeather;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.CurrentWeather;
import ru.alinadorozhkina.gbweatherapp.parcelable.entities.WeekWeather;

/**
 * класс является посредником между активити и моделью для исключения их взаимодействия напрямую
 */

public class WeatherPresenter {
    private final String LANGUAGE = "ru";
    private final String UNITS_VALUE = "metric";
    private final String API_CODE = BuildConfig.WEATHER_API_KEY;
    private WeatherInterface weatherInterface;

    public WeatherPresenter(WeatherInterface weatherInterface) {
        this.weatherInterface = weatherInterface;
    }

    public void loadDataByName(String city) {
        Log.v("weatherpresenter", " получено значение " + city);
        ApiFactory apiFactory = ApiFactory.getInstance();
        OpenWeather openWeather = apiFactory.getOpenWeather();
        openWeather.loadWeatherByName(city, LANGUAGE, UNITS_VALUE, API_CODE)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        Log.v("weatherpresenter", "by name " + response.toString());
                        new GetCurrentWeatherTask().execute(response);
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        weatherInterface.showError();
                    }
                });
    }

    public void loadDataByCoord(double lat, double lon) {
        ApiFactory apiFactory = ApiFactory.getInstance();
        OpenWeather openWeather = apiFactory.getOpenWeather();
        openWeather.loadWeatherByCoord(lat, lon, LANGUAGE, UNITS_VALUE, API_CODE)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        Log.v("weatherpresenter", "by coord " + response.toString());
                        new GetCurrentWeatherTask().execute(response);
//                        GetCurrentWeatherTask task = new GetCurrentWeatherTask(response);
//                        task.execute();
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        weatherInterface.showError();
                    }
                });
    }

    private String editDay(String data) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat fmt2 = new SimpleDateFormat("E, d MMM", Locale.getDefault());
        try {
            Date date = fmt.parse(data);
            return fmt2.format(date);
        } catch (ParseException pe) {
            return "Date";
        }
    }

    private class GetCurrentWeatherTask extends AsyncTask<Response<WeatherRequest>, Void, CurrentWeather> {
        private CurrentWeather currentWeather;

        @Override
        protected CurrentWeather doInBackground(Response<WeatherRequest>... responses) {
            Log.v("weatherpresenter", "поток " + Thread.currentThread().toString());
            // Response<WeatherRequest> request = weakReferenceRequest.get();
            if (responses[0].body() != null) {
                String cityName = responses[0].body().getCity().getName();
                Log.v("weatherpresenter", " название города " + cityName);
                int temp = (int) responses[0].body().getList()[0].getMain().getTemp();
                String description = responses[0].body().getList()[0].getWeather()[0].getDescription();
                String icon = responses[0].body().getList()[0].getWeather()[0].getIcon();
                int pressure = responses[0].body().getList()[0].getMain().getPressure();
                int wind = (int) responses[0].body().getList()[0].getWind().getSpeed();
                int humidity = responses[0].body().getList()[0].getMain().getHumidity();
                double lat = responses[0].body().getCity().getCoord().getLat();
                double lon = responses[0].body().getCity().getCoord().getLon();
                String data1 = "";
                ArrayList<WeekWeather> weekWeathersList = new ArrayList<>();
                for (int i = 0; i < responses[0].body().getList().length; i++) {
                    WeekWeather current = new WeekWeather();
                    String text = responses[0].body().getList()[i].getDt_txt();
                    String data = editDay(text);
                    if (data.equals(data1)) {
                    } else {
                        current.setDay(data);
                        data1 = data;
                        current.setTemp(responses[0].body().getList()[i].getMain().getTemp());
                        current.setIcon(responses[0].body().getList()[i].getWeather()[0].getIcon());
                        weekWeathersList.add(current);
                        Log.v("weatherpresenter", weekWeathersList.toString());
                    }
                }
                currentWeather = new CurrentWeather(cityName, temp, description, icon, wind, pressure, humidity, lat, lon, weekWeathersList);
            } else {
                Log.v("weatherpresenter", " request == null");
            }
            return currentWeather;
        }

        @Override
        protected void onPostExecute(CurrentWeather currentWeather) {
            if (currentWeather == null) {
                weatherInterface.showError();
            }
            super.onPostExecute(currentWeather);
            weatherInterface.getCurrentWeather(currentWeather);
        }
    }
}
