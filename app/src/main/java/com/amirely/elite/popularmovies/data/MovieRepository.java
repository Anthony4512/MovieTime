//package com.amirely.elite.popularmovies.data;
//
//
//import android.arch.lifecycle.LiveData;
//
//import java.util.List;
//
//import javax.inject.Inject;
//
//import models.Movie;
//
//public class MovieRepository{
//
//    private final MovieDao movieDao;
//
//
//
//    @Inject
//    public MovieRepository(MovieDao movieDao) {
//        this.movieDao = movieDao;
//    }
//
//
//
//    public LiveData<List<Movie>> getListOfMovies() {
//        return movieDao.getListOfMovies();
//    }
//
//
//    public LiveData<Movie> getMovieById(String Id) {
//        return movieDao.getMovieById(Id);
//    }
//
//
//    public void insertMovie(Movie movie) {
//        movieDao.insertMovie(movie);
//    }
//
//
//    public void deleteMovie(Movie movie) {
//        movieDao.deleteMovie(movie);
//    }
//}
