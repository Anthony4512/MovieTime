package com.amirely.elite.popularmovies;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import models.Movie;
import models.Review;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Details extends AppCompatActivity {

//    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";

    private final String API_KEY =   "1b383c179fbd530ae938ea17f25198ae"; //"YOUR API KEY GOES HERE";
    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780/";

    private ImageView moviePoster;

    String trailerId;

    private List<Review> mReviewList;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private LinearLayoutManager mLayoutManager;


    public void getFakeReviews() {
        for (int i = 0; i < 6; i++) {
            Review review = new Review("Anthony Mirely", "Schema export directory is not provided to the annotation processor so we cannot export the schema. You can either provide `room.schemaLocation` annotation processor argument OR set exportSchema to false.", "may 22, 4:12pm");
            Review review1 = new Review("Nora Reguig", "This is some text for the review... maybe i should write more to see if this comment looks better with multi-lines", "aug 23, 9:23pm");

            this.mReviewList.add(review);
            this.mReviewList.add(review1);
        }

    }



    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        setTitle("");

        //creates the back button on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mReviewList = new ArrayList<>();

        //TODO GET REVIEWS FROM API
        getFakeReviews();

        int rvId = R.id.review_RV;

        System.out.println("RV ID!!: " + rvId);



//        reviewRecyclerView = new RecyclerView(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.review_RV);

        mReviewAdapter = new ReviewAdapter(mReviewList);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mReviewAdapter);
        mRecyclerView.setAdapter(mReviewAdapter);


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
        }.execute("https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=" + API_KEY + "&language=en-US");
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


    public void readReviews(View view) {
        Intent intent = new Intent(Details.this, ReviewsActivity.class);

        startActivity(intent);

    }
}
