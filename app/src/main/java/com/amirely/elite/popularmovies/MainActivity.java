package com.amirely.elite.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import models.Movie;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListClickListener {

    private List<Movie> mMovieList;
    private RecyclerView recyclerView;
    private String SORT_BY;
    MainActivityViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = new ArrayList<>();

        model = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(gridLayoutManager);

        SORT_BY = model.getSORT_BY();

        if(model.getSORT_BY().equals("favorites")) {
            this.setTitle(getString(R.string.favorites_menu_title));
            updateMoviesFromDb();
        }
        else {
            this.setTitle(SORT_BY.equals("popular") ? getString(R.string.most_popular_menu_title) : getString(R.string.highest_rating_menu_title));

            //Lambda for onChange
            model.getMovieList(SORT_BY).observe(this, movieList -> {
                mMovieList = movieList;
                MovieAdapter adapter = new MovieAdapter(mMovieList, this);
                recyclerView.setAdapter(adapter);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SORT_BY.equals("favorites")) {
            updateMoviesFromDb();
        }
    }

    @Override
    public void onMovieClick(int movieClickedIndex) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("currentMovie", mMovieList.get(movieClickedIndex));
        startActivity(intent);
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
                model.setSORT_BY(SORT_BY);
                this.setTitle(getString(R.string.highest_rating_menu_title));
                model.changeSortBy(SORT_BY);

                return true;
            case R.id.most_popular_menu:
                SORT_BY = "popular";
                model.setSORT_BY(SORT_BY);
                this.setTitle(getString(R.string.most_popular_menu_title));
                model.changeSortBy(SORT_BY);
                return true;

            case R.id.favorites_menu:
                SORT_BY = "favorites";
                model.setSORT_BY(SORT_BY);
                this.setTitle(getString(R.string.favorites_menu_title));
                updateMoviesFromDb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void updateMoviesFromDb() {
        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getLiveMovieList().observe(this, movies -> {
                mMovieList = movies;
                MovieAdapter adapter = new MovieAdapter(movies, MainActivity.this);
                recyclerView.setAdapter(adapter);
        });
    }
}
