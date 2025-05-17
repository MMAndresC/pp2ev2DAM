package com.svalero.musicandroid.api;

import com.svalero.musicandroid.domain.Artist;
import com.svalero.musicandroid.domain.TokenResponse;
import com.svalero.musicandroid.domain.User;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ArtistsApiInterface {

    @GET("artists")
    Call<ArrayList<Artist>> getArtists(@Header("Authorization") String token);

    @GET("artists/{id}")
    Call<Artist> getArtist(@Path("id") int id, @Header("Authorization") String token);

    @GET("artists")
    Call<Artist> addArtist(@Path("userId") long userId, @Body Artist artist, @Header("Authorization") String token);

    @POST("login")
    Call<TokenResponse> loginUser(@Body User user);
}
