package com.amirely.elite.popularmovies;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amirely.elite.popularmovies.data.MovieDatabase;
import com.amirely.elite.popularmovies.utils.ApiKeyProvider;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.io.IOException;
import java.util.Objects;

import models.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Details extends AppCompatActivity {

    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780/";
    private static final String DATABASE_NAME = "movies_db";
    private MovieDatabase movieDatabase;
    private ImageView moviePoster;
    private String trailerId;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("");

        //creates the back button on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        movieDatabase = Room.databaseBuilder(getApplicationContext(),
                MovieDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        moviePoster = findViewById(R.id.movie_poster_iv);

        TextView movieRating = findViewById(R.id.user_rating_tv);
        SimpleRatingBar ratingBar = findViewById(R.id.user_rating_bar);
        TextView moviePlot = findViewById(R.id.plot_tv);
        TextView movieReleaseDate = findViewById(R.id.release_date_tv);

        final ImageView imageView = findViewById(R.id.imageButton);

        TextView readReviewsTV = findViewById(R.id.read_reviews_tv);

        final CheckBox likeIcon = findViewById(R.id.likeIcon);

        Intent intent = getIntent();

        final Movie movie = (Movie)intent.getSerializableExtra("currentMovie");
        if(movie != null) {

            likeIcon.setVisibility(View.VISIBLE);

            System.out.println("ENTERED MOVIE != NULL");

            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    int movieCount = movieDatabase.movieDao().getMovieCount(movie.getId());
                    return movieCount;
                }

                @Override
                protected void onPostExecute(Integer integer) {
                    super.onPostExecute(integer);
                    System.out.println("ENTEREd POST EXECUTE");
                    if(integer > 0) {
                        System.out.println("ENTEREd INT > 0");
                        likeIcon.setChecked(true);
                    }
                }
            }.execute();

            setTitle(movie.getTitle());

            requestTrailerId(movie);

            final String ratingText = String.valueOf(movie.getRating() + "/10");
            movieRating.setText(ratingText);

            final float ratingFloat = (float) (movie.getRating() / 2);
            ratingBar.setRating(ratingFloat);
            ratingBar.setIndicator(true);

            moviePlot.setText(movie.getPlot());
            movieReleaseDate.setText(movie.getReleaseDate().substring(0, 4));

            readReviewsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Details.this, ReviewsActivity.class);
                    intent.putExtra("movieId", movie.getId());
                    startActivity(intent);
                }
            });

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

            //listen for click on the trailer image and make request to play trailer in youtube
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchYoutubeVideo(imageView.getContext(), trailerId);
                }
            });
            likeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(likeIcon.isChecked()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                movieDatabase.movieDao().insertMovie(movie);
                            }
                        }) .start();
                    }
                    else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                movieDatabase.movieDao().deleteMovie(movie);
                            }
                        }) .start();
                    }
                    Toast.makeText(Details.this, "VALUE OF LIKE: " + likeIcon.isChecked(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            likeIcon.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *
     * @param outState Bundle that is going to be saved when the activity dies
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //ends the current activity and return true when the back button is pressed
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private void requestTrailerId(Movie movie) {

        new AsyncTask<String, Void, String>() {

            final OkHttpClient client = new OkHttpClient();

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String results = run(strings[0]);
                    trailerId = JsonUtils.getTrailersFromId(results);
                    return"";

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            String run(String url) throws IOException {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();
                return Objects.requireNonNull(response.body()).string();
            }
        }.execute("https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=" + ApiKeyProvider.API_KEY + "&language=en-US");
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));

        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    private void addDrawable(Drawable drawable) {
        moviePoster.setImageDrawable(drawable);
    }
}
