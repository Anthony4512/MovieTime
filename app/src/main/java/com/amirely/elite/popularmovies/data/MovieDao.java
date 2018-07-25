package com.amirely.elite.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import models.Movie;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getListOfMovies();

    @Query("SELECT * FROM Movie WHERE id=:Id")
    LiveData<Movie> getMovieById(String Id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT COUNT(*) FROM Movie WHERE id=:Id")
    int getMovieCount(String Id);
}
