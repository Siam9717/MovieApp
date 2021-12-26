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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {

    Result result;

    OkHttpClient client = new OkHttpClient();
    CastResponse castResponse;

    String getData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        result = (Result) getIntent().getSerializableExtra("result");

        TextView movieNames = findViewById(R.id.movies_names);
        movieNames.setText(result.getTitle());

        TextView movieDesc = findViewById(R.id.movie_synopsis);
        movieDesc.setText(result.getOverview());

        ImageView poster = findViewById(R.id.poster_small);

        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500"+ result.getPosterPath())
                .centerCrop()
                .into(poster);

        RecyclerView movieCast = findViewById(R.id.castRV);
        movieCast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));


        try {
            String url = "https://api.themoviedb.org/3/movie/"+result.getId()+"/credits?api_key=3fa9058382669f72dcb18fb405b7a831";
            System.out.println("URL "+url);
            String data = getData(url);

            castResponse = new Gson().fromJson(data, CastResponse.class);
            movieCast.setAdapter(new NewAdapter());

        } catch (IOException e) {
            e.printStackTrace();
        }



        //

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