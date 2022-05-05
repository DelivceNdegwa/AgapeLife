package com.example.agapelife.networking.services;

import android.content.Context;

import com.example.agapelife.utils.netutils.NetworkConnectionInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static ServiceGenerator mInstance;
    private static final String BASE_URL = URLs.API_URL;
    private static Context context;
    private Retrofit retrofit;


    private ServiceGenerator(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
//                .addInterceptor(new NetworkConnectionInterceptor(context))
                .build();

//        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static synchronized ServiceGenerator getInstance(){
        if(mInstance == null){
            mInstance =  new ServiceGenerator();
        }
        return mInstance;
    }

    public EndPoints getApiConnector(){
        return retrofit.create(EndPoints.class);
    }

}
