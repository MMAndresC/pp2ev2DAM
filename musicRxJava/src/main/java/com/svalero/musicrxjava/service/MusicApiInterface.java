package com.svalero.musicrxjava.service;

import com.svalero.musicrxjava.domain.Album;
import com.svalero.musicrxjava.domain.Artist;
import com.svalero.musicrxjava.domain.Token;
import com.svalero.musicrxjava.domain.User;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface MusicApiInterface {

    @GET("artists")
    Single<List<Artist>> getAllArtists();

    @POST("login")
    Single<Token> login(@Body User user);

    @GET("artists/{idArtist}/albums")
    Single<List<Album>> getAllAlbums(@Path("idArtist") long idArtist);
}
