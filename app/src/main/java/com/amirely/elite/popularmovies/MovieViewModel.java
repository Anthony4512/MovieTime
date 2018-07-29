package com.amirely.elite.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.amirely.elite.popularmovies.data.MovieDatabase;

import java.util.List;

import models.Movie;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> liveMovieList;
    private MovieDatabase movieDatabase;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieDatabase = MovieDatabase.getInstance(this.getApplication());
    }

    public LiveData<List<Movie>> getLiveMovieList() {
        liveMovieList = movieDatabase.movieDao().getListOfMovies();
        return liveMovieList;
    }
}
