package com.amirely.elite.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import models.Review;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ReviewsActivity extends AppCompatActivity {


    private List<Review> mReviewList;
    private RecyclerView mRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private LinearLayoutManager mLayoutManager;

    private final String API_KEY = "YOUR API KEY GOES HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();

        String movieId = intent.getStringExtra("movieId");

        System.out.println(movieId);

//        requestMoviesReviews(movieId);

        mRecyclerView = findViewById(R.id.review_RV);

        mReviewAdapter = new ReviewAdapter(mReviewList);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mReviewAdapter);
        mRecyclerView.setAdapter(mReviewAdapter);

        requestMoviesReviews(movieId);

    }

    private void adapterSwap() {
        ReviewAdapter reviewAdapter = new ReviewAdapter(mReviewList);
        mRecyclerView.swapAdapter(reviewAdapter, false);
    }


    @SuppressLint("StaticFieldLeak")
    private void requestMoviesReviews(String movieId) {

        new AsyncTask<String, Void, String>() {

            final OkHttpClient client = new OkHttpClient();

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String results = run(strings[0]);

                    mReviewList = JsonUtils.getMovieReviews(results);

                    return"";

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                adapterSwap();
            }

            String run(String url) throws IOException {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();

                return Objects.requireNonNull(response.body()).string();
            }
        }.execute("https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" + API_KEY + "&language=en-US");
    }

}
