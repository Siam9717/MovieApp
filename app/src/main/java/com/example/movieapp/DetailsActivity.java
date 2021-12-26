package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        result = (Result) getIntent().getSerializableExtra("result");


        RecyclerView movieCast = findViewById(R.id.cast_poster);
        movieCast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        movieCast.setAdapter(new NewAdapter());

    }

    class NewAdapter extends RecyclerView.Adapter<CastViewHolder>{

        @NonNull
        @Override
        public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View newView = LayoutInflater.from(DetailsActivity.this).inflate(R.layout.movie_desc, parent, false);
            return new CastViewHolder(newView);
        }

        @Override
        public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {

            return 0;
        }
    }


    class CastViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPoster;
        TextView mNames;
        ImageView smallPoster;
        TextView nPageRating;
        TextView synopsis;
        ImageView imgCast;
        TextView nameCast;


        public CastViewHolder(@NonNull View itemView) {

            super(itemView);
            imgPoster = itemView.findViewById(R.id.poster_image);
            mNames = itemView.findViewById(R.id.movies_names);
            smallPoster = itemView.findViewById(R.id.poster_small);
            nPageRating = itemView.findViewById(R.id.next_page_rating);
            synopsis = itemView.findViewById(R.id.movie_synopsis);
            imgCast = itemView.findViewById(R.id.cast_image);
            nameCast = itemView.findViewById(R.id.cast_name);

        }
    }
}