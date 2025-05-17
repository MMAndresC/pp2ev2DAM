package com.svalero.musicandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.musicandroid.adapter.ArtistAdapter;
import com.svalero.musicandroid.contract.ArtistListContract;
import com.svalero.musicandroid.contract.UserLoginContract;
import com.svalero.musicandroid.domain.Artist;

import com.svalero.musicandroid.R;
import com.svalero.musicandroid.domain.TokenResponse;
import com.svalero.musicandroid.domain.User;
import com.svalero.musicandroid.presenter.ArtistListPresenter;
import com.svalero.musicandroid.presenter.UserLoginPresenter;
import com.svalero.musicandroid.util.SharedPreferencesUtil;

import java.util.ArrayList;

public class ArtistListView extends AppCompatActivity implements ArtistListContract.View, UserLoginContract.View {

    private ArtistAdapter artistAdapter;
    private ArrayList<Artist> artistList;
    private ArtistListContract.Presenter presenter;
    private RecyclerView artistsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserLoginContract.Presenter presenterLogin = new UserLoginPresenter(this);
        presenterLogin.loginUser(new User("demo@example.com", "1234"));

        artistList = new ArrayList<>();

        artistsView = findViewById(R.id.artists_view);
        artistsView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        artistsView.setLayoutManager(linearLayoutManager);
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        artistList.clear();
        presenter.loadArtists();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    // public boolean onOptionsItemSelected(@NonNull MenuItem item)

    public void registerArtist(View view) {
        Intent intent = new Intent(this, RegisterArtistView.class);
        startActivity(intent);
    }

    @Override
    public void listArtists(ArrayList<Artist> artistList) {
        this.artistList = artistList;
        artistAdapter = new ArtistAdapter(artistList);
        artistsView.setAdapter(artistAdapter);
    }

    @Override
    public void getSessionToken(TokenResponse token) {
        SharedPreferencesUtil.setCustomSharedPreferences(this, "token", token.getToken());
        SharedPreferencesUtil.setCustomSharedPreferences(this, "email", "demo@example.com");
        String completeToken = "Bearer " + token.getToken();
        presenter = new ArtistListPresenter(this);
        presenter.loadArtists(completeToken);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }





}
