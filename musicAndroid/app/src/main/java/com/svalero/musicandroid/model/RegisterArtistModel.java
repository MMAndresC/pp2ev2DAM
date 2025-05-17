package com.svalero.musicandroid.model;

import com.svalero.musicandroid.api.ArtistsApi;
import com.svalero.musicandroid.api.ArtistsApiInterface;
import com.svalero.musicandroid.contract.RegisterArtistContract;
import com.svalero.musicandroid.domain.Artist;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterArtistModel implements RegisterArtistContract.Model {
    @Override
    public void registerArtist(Artist artist, String token, OnRegisterArtistListener listener) {
        ArtistsApiInterface artistsApi = ArtistsApi.buildInstance();
        Call<Artist> callRegisterArtist = artistsApi.addArtist(1, artist, token);
        callRegisterArtist.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Artist> call, Response<Artist> response) {
                switch (response.code()) {
                    case 201:
                        listener.onRegisterArtistSuccess(response.body());
                        break;
                    case 400:
                        break;
                    case 500:
                        listener.onRegisterArtistError("Internal API error: " + response.message());
                        break;
                    default:
                        listener.onRegisterArtistError("API inovation error: " + response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Artist> call, Throwable t) {
                listener.onRegisterArtistError("Unable to connect to the data source.  Check the connection and try again");
            }
        });

    }
}
