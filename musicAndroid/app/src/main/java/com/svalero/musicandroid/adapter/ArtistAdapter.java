package com.svalero.musicandroid.adapter;

import static android.view.View.inflate;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.musicandroid.R;
import com.svalero.musicandroid.domain.Artist;
import com.svalero.musicandroid.view.DetailActivityView;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolder> {

    private List<Artist> artistList;

    public ArtistAdapter(List<Artist> artistList) { this.artistList = artistList;}

    @NonNull
    @Override
    public ArtistAdapter.ArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.artistsview_item, parent, false);
        return new ArtistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ArtistHolder holder, int position) {
        holder.name.setText(artistList.get(position).getName());
        holder.country.setText(artistList.get(position).getCountry());
    }

    @Override
    public int getItemCount() { return artistList.size();}

    public class ArtistHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView country;

        public ArtistHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            country = itemView.findViewById(R.id.item_country);

            itemView.setOnClickListener(view -> {
                Artist artist = artistList.get(getAdapterPosition());
                Intent intent = new Intent(itemView.getContext(), DetailActivityView.class);
                intent.putExtra("artist", artist);


                    startActivity(itemView.getContext(), intent, null);
            });
        }
    }
}
