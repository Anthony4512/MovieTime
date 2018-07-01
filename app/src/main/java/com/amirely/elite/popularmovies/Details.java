package com.amirely.elite.popularmovies;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.io.IOException;
import java.util.Objects;

import Models.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Details extends AppCompatActivity {

//    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";


    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780/";


    private ImageView moviePoster;

    String trailerId;



    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        try
//        {
//            Objects.requireNonNull(this.getSupportActionBar()).hide();
//        }
//        catch (NullPointerException ignored){}

        setContentView(R.layout.activity_details);
        setTitle("");






        moviePoster = findViewById(R.id.movie_poster_iv);
//        TextView movieTitle = findViewById(R.id.movie_title_tv);
        TextView movieRating = findViewById(R.id.user_rating_tv);
        SimpleRatingBar ratingBar = findViewById(R.id.user_rating_bar);
        TextView moviePlot = findViewById(R.id.plot_tv);
        TextView movieReleaseDate = findViewById(R.id.release_date_tv);

        final ImageView imageView = findViewById(R.id.imageButton);




        Intent intent = getIntent();

        Movie movie = (Movie)intent.getSerializableExtra("currentMovie");
        if(movie != null) {
            setTitle(movie.getTitle());

            requestTrailerId(movie);


//        System.out.println("POSTER STRING: " + movie.getPosterString());


//        movieTitle.setText(movie.getTitle());

            final String ratingText = String.valueOf(movie.getRating() + "/10");
            movieRating.setText(ratingText);

            //TODO add a listener to the rating bar to be able to rate the movie in real time
            final float ratingFloat = (float) (movie.getRating() / 2);
            ratingBar.setRating(ratingFloat);
            ratingBar.setIndicator(true);

            moviePlot.setText(movie.getPlot());
            movieReleaseDate.setText(movie.getReleaseDate().substring(0, 4));



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

            System.out.println("TRAILER ID: " + trailerId);


            //listen for click on the trailer image and make request to play trailer in youtube
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchYoutubeVideo(imageView.getContext(), trailerId);
                }
            });

        }


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
        }.execute("https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=1b383c179fbd530ae938ea17f25198ae&language=en-US");
    }


    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
//        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "ZJDMWVZta3M"));

        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));

//        Intent webIntent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("http://www.youtube.com/watch?v=" + "ZJDMWVZta3M"));

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
