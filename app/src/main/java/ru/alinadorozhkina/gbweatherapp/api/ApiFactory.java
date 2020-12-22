package ru.alinadorozhkina.gbweatherapp.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.alinadorozhkina.gbweatherapp.interfaces.OpenWeather;

/**
 * Создаем объект ApiFactory, который будет создавать ретрофит.
 * Чтобы объект ретрофит был единственным, создаю паттерн синглтон.
 * Ретрофит и GSON ответственны за получение и преобразование данных.
 * RxJava устанавливает слушатель, прошло ли все успешно, или есть ошибка
 */
public class ApiFactory {
    private static ApiFactory apiFactory;
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://api.openweathermap.org/";

    private ApiFactory(){
        retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static ApiFactory getInstance(){
        if (apiFactory==null){
            apiFactory=new ApiFactory();
        }
        return apiFactory;
    }

    public OpenWeather getOpenWeather(){
        return retrofit.create(OpenWeather.class);
    }
}
