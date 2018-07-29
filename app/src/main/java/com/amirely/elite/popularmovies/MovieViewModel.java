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

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        liveMovieList = movieDatabase.movieDao().getListOfMovies();
    }

    public LiveData<List<Movie>> getLiveMovieList() {
        return liveMovieList;
    }
}
