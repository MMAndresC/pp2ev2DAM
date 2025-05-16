package com.svalero.musicandroid.contract;

import com.svalero.musicandroid.domain.Artist;

public interface RegisterArtistContract {

    interface Model {
        interface onRegisterArtistListener {
            void onRegisterArtistSuccess(Artist registeredArtist);
            void onRegisterArtistError(String message);
        }
        void registerArtist(Artist artist, onRegisterArtistListener listener);
    }

    interface View {
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void registerArtist(Artist artist);
    }
}
