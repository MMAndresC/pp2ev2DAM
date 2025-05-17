package com.svalero.musicrxjava.service;

import com.svalero.musicrxjava.constants.Constants;
import com.svalero.musicrxjava.domain.Album;
import com.svalero.musicrxjava.domain.Artist;
import com.svalero.musicrxjava.domain.Token;
import com.svalero.musicrxjava.utils.SessionManager;
import io.reactivex.rxjava3.core.Observable;

public class MusicService {

    private final MusicApiInterface api;

    public MusicService() {
        // Build retrofit without token because until received login response there is not login
        this.api = ApiService.buildInstance("").create(MusicApiInterface.class);
    }

    public Observable<Token> login() {
        return this.api.login(Constants.DEMO_USER)
                .toObservable()
                .doOnNext(tokenObj -> {
                    System.out.println("Received token: " + tokenObj.getToken());
                    // Save token in session
                    SessionManager.getInstance().setAuthToken("Bearer " + tokenObj.getToken());
                })
                .doOnError(error -> {
                    System.out.println("Login failed: " + error.getMessage());
                });
    }

    public Observable<Artist> getAllArtists() {
        return this.api.getAllArtists()
                .toObservable()
                .doOnNext(response -> {
                    System.out.println("Artists received");
                })
                .flatMapIterable(artists -> artists);
    }

    // First send login request to obtain token to authorize next requests
    public Observable<Artist> loginAndGetAllArtists() {
        return login()
                .flatMap(token -> getAllArtists());
    }

    public Observable<Album> getAlbumsByIdArtist(long idArtist) {
        return this.api.getAllAlbums(idArtist)
                .toObservable()
                .doOnNext(response -> {
                    System.out.println("Albums received");
                })
                .flatMapIterable(albums -> albums);
    }
}
