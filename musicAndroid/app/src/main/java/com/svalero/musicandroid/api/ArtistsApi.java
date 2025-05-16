package com.svalero.musicandroid.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtistsApi {
    private static final String URL = "http://172.17.21.93:8090/api/v1/";
    public static ArtistsApiInterface buildInstance() {

        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ArtistsApiInterface.class);

    }
}
