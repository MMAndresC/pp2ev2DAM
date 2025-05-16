package com.svalero.musicandroid.model;



import com.svalero.musicandroid.api.ArtistsApi;
import com.svalero.musicandroid.api.ArtistsApiInterface;
import com.svalero.musicandroid.contract.ArtistListContract;
import com.svalero.musicandroid.domain.Artist;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistListModel implements ArtistListContract.Model {

    @Override
    public void loadArtists (String token, OnLoadArtistsListener listener) {
        ArtistsApiInterface artistsApi = ArtistsApi.buildInstance();
        Call<ArrayList<Artist>> getArtistsCall = artistsApi.getArtists(token);
        getArtistsCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ArrayList<Artist>> call, Response<ArrayList<Artist>> response) {
                if (response.code() == 200) {
                    listener.onLoadArtistsSuccess(response.body());
                } else if (response.code() == 500) {
                    listener.onLoadArtistsError("The API is not available.  Please try again");
                } else {
                    listener.onLoadArtistsError(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Artist>> call, Throwable t) {
                listener.onLoadArtistsError("Could not connect to the data source.  Please check the connection and try again");
            }

        });
    }
}
