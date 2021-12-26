package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
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

    String run(String url) throws IOException {
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

//        Not for long run. Might crash app
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_details);

        result = (Result) getIntent().getSerializableExtra("result");

        TextView movieNames = findViewById(R.id.movies_names);
        movieNames.setText(result.getTitle());

        TextView nPageRating = findViewById(R.id.next_page_rating);
        nPageRating.setText("" +result.getVoteAverage());

        TextView movieDesc = findViewById(R.id.movie_synopsis);
        movieDesc.setText(result.getOverview());

        ImageView sPoster = findViewById(R.id.poster_small);

        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500"+ result.getPosterPath())
                .centerCrop()
                .into(sPoster);

        ImageView bPoster = findViewById(R.id.poster_image);

        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500"+ result.getPosterPath())
                .centerCrop()
                .into(bPoster);



        RecyclerView movieCast = findViewById(R.id.castRV);
        movieCast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));


        try {
            String data = run("https://api.themoviedb.org/3/movie/" + result.getId()+ "/credits?api_key=3fa9058382669f72dcb18fb405b7a831");

            castResponse = new Gson().fromJson(data, castResponse.getClass());
            movieCast.setAdapter(new NewAdapter());

        } catch (IOException e) {
            e.printStackTrace();
        }


       // movieCast.setAdapter(new NewAdapter());

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

            holder.nameCast.setText(""+castResponse.getCast());

            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500"+ castResponse.getCast())
                    .centerCrop()
                    .into(holder.imgCast);

        }

        @Override
        public int getItemCount() {
            return 0;  //castResponse.getCast().size();
        }
    }


    class CastViewHolder extends RecyclerView.ViewHolder {


        ImageView imgCast;
        TextView nameCast;


        public CastViewHolder(@NonNull View itemView) {

            super(itemView);

            imgCast = itemView.findViewById(R.id.cast_image);
            nameCast = itemView.findViewById(R.id.cast_name);

        }
    }
}