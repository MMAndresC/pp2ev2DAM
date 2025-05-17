package com.svalero.musicandroid.presenter;

import com.svalero.musicandroid.contract.ArtistListContract;
import com.svalero.musicandroid.domain.Artist;
import com.svalero.musicandroid.model.ArtistListModel;

import java.util.ArrayList;

public class ArtistListPresenter implements ArtistListContract.Presenter, ArtistListContract.Model.OnLoadArtistsListener {

    private ArtistListContract.View view;
    private ArtistListContract.Model model;

    public ArtistListPresenter(ArtistListContract.View view) {
        this.view = view;
        model = new ArtistListModel();
    }

    @Override
    public void loadArtists(String token) {
        model.loadArtists(token, this);
    }

    @Override
    public void onLoadArtistsSuccess(ArrayList<Artist> artistList) {
        view.listArtists(artistList);
    }

    @Override
    public void onLoadArtistsError(String message) {
        view.showErrorMessage(message);
    }

}
