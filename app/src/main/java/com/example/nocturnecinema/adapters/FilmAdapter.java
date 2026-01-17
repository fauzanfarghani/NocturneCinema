package com.example.nocturnecinema.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nocturnecinema.DetailActivity;
import com.example.nocturnecinema.R;
import com.example.nocturnecinema.models.Film;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    private Context context;
    private List<Film> filmList;

    public FilmAdapter(Context context, List<Film> filmList) {
        this.context = context;
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = filmList.get(position);
        holder.tvTitle.setText(film.getTitle());
        holder.tvRating.setText("Rating: " + film.getRating());
        
        // Load Image
        com.example.nocturnecinema.utils.ImageLoader.loadImage(film.getCoverUrl(), holder.ivCover);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("FILM_DATA", film);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public static class FilmViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvRating;
        ImageView ivCover;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_film_title);
            tvRating = itemView.findViewById(R.id.tv_film_rating);
            ivCover = itemView.findViewById(R.id.iv_film_cover);
        }
    }
}
