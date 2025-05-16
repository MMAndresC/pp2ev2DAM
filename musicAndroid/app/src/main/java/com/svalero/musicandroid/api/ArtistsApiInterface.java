package com.svalero.musicandroid.api;

import com.svalero.musicandroid.domain.Artist;

import java.util.ArrayList;

public interface ArtistsApiInterface {

    @GET("artists")
    Call<ArrayList<Artist>> getArtists();

    @GET("artists/{id}")
    Call<Artist> getArtist(@Path("id") int id);

    @GET("artists")
    Call<Artist> addArtist(@Path("userId") long userId, @Body Artist artist);
}
