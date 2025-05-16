package com.svalero.musicandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.svalero.musicandroid.R;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Intent intent = getIntent();
        long artistId = intent.getLongExtra("artistId", 0);
        printArtist(artistId);
    }

    private void printArtist(long artistId) {
        ((TextView) findViewById(R.id.artist_id)).setText(String.valueOf(artistId));
    }
}
