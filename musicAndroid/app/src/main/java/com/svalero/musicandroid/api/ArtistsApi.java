package com.svalero.musicandroid.api;

public class ArtistsApi {
    private static final String URL = "http://172.17.21.31:8080/api(v1/";
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
