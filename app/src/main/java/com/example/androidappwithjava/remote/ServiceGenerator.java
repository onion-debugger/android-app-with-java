package com.example.androidappwithjava.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ServiceGenerator {
    private static final String BASE_URL = "https://pokeapi.co/api/v2";

    // Builder
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient());

    public Object createService(Class serviceClass) {
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
