package com.amirely.elite.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import Models.Movie;

public class Details extends AppCompatActivity {

    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";

    private ImageView moviePoster;



    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        moviePoster = findViewById(R.id.movie_poster_iv);
        TextView movieTitle = findViewById(R.id.movie_title_tv);
        TextView movieRating = findViewById(R.id.user_rating_tv);
        TextView moviePlot = findViewById(R.id.plot_tv);
        TextView movieReleaseDate = findViewById(R.id.release_date_tv);

        Intent intent = getIntent();

        Movie movie = (Movie)intent.getSerializableExtra("currentMovie");

        System.out.println("POSTER STRING: " + movie.getPosterString());



        movieTitle.setText(movie.getTitle());
        movieRating.setText(String.valueOf(movie.getRating()));
        moviePlot.setText(movie.getPlot());
        movieReleaseDate.setText(movie.getReleaseDate());

        new AsyncTask<String, Void, Drawable>() {


            @Override
            protected Drawable doInBackground(String... strings) {
                return JsonUtils.LoadImageFromWebOperations(IMAGE_BASE_URL + strings[0]);
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                super.onPostExecute(drawable);
                addDrawable(drawable);

            }

        }.execute(movie.getPosterString());


    }

    private void addDrawable(Drawable drawable) {
        moviePoster.setImageDrawable(drawable);
    }
}
