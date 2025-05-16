package com.svalero.musicandroid.contract;

import com.svalero.musicandroid.domain.Artist;

import java.util.ArrayList;

public interface ArtistListContract {

    void loadArtists(String token);

    interface Model {
        interface OnLoadArtistsListener {
            void onLoadArtistsSuccess(ArrayList<Artist> artistList);
            void onLoadArtistsError(String message);
        }
        void loadArtists(String token, OnLoadArtistsListener listener);
    }

    interface View {
        void listArtists(ArrayList<Artist> artistList);
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void loadArtists(String token);
    }
}
