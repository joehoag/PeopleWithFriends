package com.joeh.peoplewithfriends;

import android.app.Application;

import okhttp3.OkHttpClient;

/**
 * Override the Application class to allow us to spin up local managers
 */
public class MainApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Create an OkHttpClient, then spin up our Retrofit client
        OkHttpClient client = new OkHttpClient.Builder().build(); // No interceptors necessary
        RetrofitClient.initialize( client );
    }
}
