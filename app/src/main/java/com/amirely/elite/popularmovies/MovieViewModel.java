package com.amirely.elite.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amirely.elite.popularmovies.data.MovieDatabase;

import java.util.List;

import models.Movie;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> liveMovieList;
    private String TAG = MovieViewModel.class.getSimpleName();

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the database");

        liveMovieList = movieDatabase.movieDao().getListOfMovies();
    }

    public LiveData<List<Movie>> getLiveMovieList() {
        return liveMovieList;
    }
}
