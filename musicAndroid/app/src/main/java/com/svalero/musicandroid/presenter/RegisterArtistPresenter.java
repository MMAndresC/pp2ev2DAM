package com.svalero.musicandroid.presenter;

import com.svalero.musicandroid.contract.RegisterArtistContract;
import com.svalero.musicandroid.domain.Artist;
import com.svalero.musicandroid.model.RegisterArtistModel;

public class RegisterArtistPresenter implements RegisterArtistContract.Presenter, RegisterArtistContract.Model.OnRegisterArtistListener{

    private RegisterArtistContract.Model model;
    private RegisterArtistContract.View view;

    public RegisterArtistPresenter(RegisterArtistContract.View view) {
        this.model = new RegisterArtistModel();
        this.view = view;
    }

    @Override
    public void registerArtist(Artist artist, String token) {
        if (artist.getName().isEmpty()) {
            view.showErrorMessage("The mark field cannot be empty");
            return;
        }

        model.registerArtist(artist, token,this);
    }

    @Override
    public void onRegisterArtistSuccess(Artist registeredArtist) {
        view.showSuccessMessage("Artist correctly registered with the identifier" + registeredArtist.getId());
    }

    @Override
    public void onRegisterArtistError(String message) {
        view.showErrorMessage(message);
    }
}
