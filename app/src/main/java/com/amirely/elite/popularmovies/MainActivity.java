package com.amirely.elite.popularmovies;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.amirely.elite.popularmovies.data.MovieDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import models.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListClickListener {

    //replace the string with the api key to be able to use the app
    private final String API_KEY = "1b383c179fbd530ae938ea17f25198ae"; // "YOUR API KEY GOES HERE";

    private List<Movie> mMovieList;
    private RecyclerView recyclerView;
    private String SORT_BY;
    private String MOVIES_URL;
    private MovieAdapter mMovieAdapter;


    private static final String DATABASE_NAME = "movies_db";
    private MovieDatabase movieDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SORT_BY = "popular";

        MOVIES_URL = "https://api.themoviedb.org/3/movie/" + SORT_BY + "?api_key=" + API_KEY + "&language=en-US&page=1";

        mMovieList = new ArrayList<>();

        movieDatabase = Room.databaseBuilder(getApplicationContext(),
                MovieDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();



        new MovieFetcher().execute(MOVIES_URL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);

        recyclerView = findViewById(R.id.recycler_view);

        mMovieAdapter = new MovieAdapter(mMovieList, this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    public void onMovieClick(int movieClickedIndex) {
        Intent intent = new Intent(MainActivity.this, Details.class);
        intent.putExtra("currentMovie", mMovieList.get(movieClickedIndex));

        startActivity(intent);
    }

    private void adapterSwap() {
        MovieAdapter movieAdapter = new MovieAdapter(mMovieList, this);
        recyclerView.swapAdapter(movieAdapter, false);
    }

    @SuppressLint("StaticFieldLeak")
    class MovieFetcher extends AsyncTask<String, Void, String> {

        final OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {
            try {
                String results = run(strings[0]);
                mMovieList = JsonUtils.parseMoviesJson(results);
                return "";
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.highest_rating_menu:
                SORT_BY = "top_rated";
                updateMovieUrl(SORT_BY);
                new MovieFetcher().execute(this.MOVIES_URL);
                mMovieAdapter.notifyDataSetChanged();
                return true;
            case R.id.most_popular_menu:
                SORT_BY = "popular";
                updateMovieUrl(SORT_BY);
                new MovieFetcher().execute(this.MOVIES_URL);
                mMovieAdapter.notifyDataSetChanged();
                return true;

            case R.id.favorites_menu:
                SORT_BY = "favorites";
                updateMoviesFromDb();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void updateMoviesFromDb() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mMovieList = movieDatabase.movieDao().getListOfMovies();
//                mMovieAdapter.notifyDataSetChanged();
//            }
//        }) .start();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                mMovieList = movieDatabase.movieDao().getListOfMovies();
                mMovieAdapter.notifyDataSetChanged();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapterSwap();
            }

            //            final OkHttpClient client = new OkHttpClient();
//
//            @Override
//            protected Void doInBackground(String... strings) {
//                try {
//                    String results = run(strings[0]);
//
//                    trailerId = JsonUtils.getTrailersFromId(results);
//
//                    return"";
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }

//            String run(String url) throws IOException {
//                Request request = new Request.Builder()
//                        .url(url)
//                        .build();
//
//                Response response = client.newCall(request).execute();
//
//                return Objects.requireNonNull(response.body()).string();
//            }
        }.execute();
    }

    public void updateMovieUrl(String sortBy) {
        this.MOVIES_URL = "https://api.themoviedb.org/3/movie/" + sortBy + "?api_key=" + API_KEY + "&language=en-US&page=1";
    }
}
