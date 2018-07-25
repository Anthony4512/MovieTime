package com.amirely.elite.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.amirely.elite.popularmovies.utils.ApiKeyProvider;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();

        String movieId = intent.getStringExtra("movieId");

        mRecyclerView = findViewById(R.id.review_RV);

        ReviewAdapter mReviewAdapter = new ReviewAdapter(mReviewList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

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
                if(mReviewList.isEmpty()) {
                    Toast.makeText(ReviewsActivity.this, "THERE IS NO REVIEWS FOR THIS MOVIE", Toast.LENGTH_LONG).show();
                }
                else {
                    adapterSwap();
                }

            }

            String run(String url) throws IOException {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();

                return Objects.requireNonNull(response.body()).string();
            }
        }.execute("https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" + ApiKeyProvider.API_KEY + "&language=en-US");
    }

}
