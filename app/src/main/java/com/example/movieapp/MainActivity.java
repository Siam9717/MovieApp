package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

//    String [] movieName = {"Seven Samurai", "Lord of the Rings", "Harakiri", "Godfather", "Amar Bondhu Rashed"};
//    int [] img = {R.mipmap.img_seven_samurai, R.mipmap.img_lord_of_the_rings, R.mipmap.img_harakiri, R.mipmap.img_godfather, R.mipmap.img_amar_bondhu_rashed};

    PopularMovieResponse movieResponse;


    OkHttpClient client = new OkHttpClient();

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

        //Not for long run. Might crash app
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        RecyclerView movieRV = findViewById(R.id.movie_list_view);
        movieRV.setLayoutManager(new GridLayoutManager(this, 2));
        movieRV.setAdapter(new MyAdapter());

        try {
            String data = run("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=1&api_key=3fa9058382669f72dcb18fb405b7a831&language=en-US");

            movieResponse = new Gson().fromJson(data, PopularMovieResponse.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class MyAdapter extends RecyclerView.Adapter<MovieViewHolder>{


        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item, parent, false);
            return new MovieViewHolder(v);
            //decides how the view will look. Connects list_item.xml here
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

            holder.textView.setText(movieResponse.getResults().get(position).getTitle());
            holder.rating.setText("" +movieResponse.getResults().get(position).getVoteAverage());

            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500"+ movieResponse.getResults().get(position).getPosterPath())
                    .centerCrop()
                    .into(holder.movieImg);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                    i.putExtra("result", movieResponse.getResults().get(position));
                    startActivity(i);

                }
            });

            //holder.movieImg.setImageResource(img[position]);
            //set the data on the layout. Set the content. Replace according to array list
        }

        @Override
        public int getItemCount() {
            return movieResponse.getResults().size();
            //Gets called first. Decides how many times the upper two override methods will run
            //How many times list_item.xml will run
        }
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView movieImg;
        TextView rating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title); //can't access findViewById directly so use itemView to access textView from list_item.xml
            movieImg = itemView.findViewById(R.id.poster);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}