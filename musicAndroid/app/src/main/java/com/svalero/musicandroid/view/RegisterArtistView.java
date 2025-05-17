package com.svalero.musicandroid.view;

import android.view.View;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.musicandroid.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.svalero.musicandroid.contract.RegisterArtistContract;
import com.svalero.musicandroid.domain.Artist;
import com.svalero.musicandroid.presenter.RegisterArtistPresenter;
import com.svalero.musicandroid.util.SharedPreferencesUtil;

public class RegisterArtistView extends AppCompatActivity implements RegisterArtistContract.View {

    private RegisterArtistPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_artist);

        presenter = new RegisterArtistPresenter(this);
    }


    public void register(View view) {
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String country = ((EditText) findViewById(R.id.country)).getText().toString();
        Artist artist = new Artist(name, country);
        String token = SharedPreferencesUtil.getCustomSharedPreferences(this, "token");
        if(token == null){
            String message = "Required token";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }
        token = "Bearer " + token;
        presenter.registerArtist(artist, token);
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(findViewById(R.id.add_artist_button), message, BaseTransientBottomBar.LENGTH_INDEFINITE).show();
    }


    @Override
    public void showSuccessMessage(String message) {
        Snackbar.make(findViewById(R.id.add_artist_button), message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }


    }



