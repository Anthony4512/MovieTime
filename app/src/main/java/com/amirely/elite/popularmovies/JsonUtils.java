package com.amirely.elite.popularmovies;


import android.graphics.drawable.Drawable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import models.Movie;
import models.Review;

class JsonUtils {

    private final static String RATING = "vote_average";
    private final static String TITLE = "title";
    private final static String PLOT = "overview";
    private final static String RELEASE_DATE =  "release_date";
    private final static String POSTER_PATH = "poster_path";
    private final static String MOVIE_ID = "id";

    public static List<Movie> parseMoviesJson(String json) {
        try {

            JSONObject jsonMovies = new JSONObject(json);
            List<Movie> moviesList = new ArrayList<>();
            JSONArray jsonMoviesArray = jsonMovies.getJSONArray("results");


            if(null != jsonMoviesArray) {
                for (int i = 0; i < jsonMoviesArray.length(); i++) {

                    JSONObject movieObject = jsonMoviesArray.getJSONObject(i);

                    String title = movieObject.optString(TITLE);
                    double rating = movieObject.optDouble(RATING);
                    String release_date = movieObject.optString(RELEASE_DATE);
                    String plot = movieObject.optString(PLOT);
                    String imageUrl = movieObject.optString(POSTER_PATH);
                    String movieId = movieObject.optString(MOVIE_ID);

                    Movie movie = new Movie(movieId, imageUrl, title, rating, plot, release_date);

//                    System.out.println("ID = " + movie.getId());

                    moviesList.add(movie);
                }
            }
            else {
                Log.d("JSON MOVIE ARRAY","JSONMOVIES ARRAY IS EMPTY OR NULL");
            }
            return moviesList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTrailersFromId(String json) {
        try {
            JSONObject videoJsonObject = new JSONObject(json);
            JSONArray  videoJsonArray = videoJsonObject.getJSONArray("results");
            JSONObject movieObject = videoJsonArray.getJSONObject(0);

            String videoId = movieObject.optString("key");

            return videoId;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Review> getMovieReviews(String json) {
        try {
            List<Review> reviewList = new ArrayList<>();
            JSONObject reviewJsonObject = new JSONObject(json);
            JSONArray  reviewsJsonArray = reviewJsonObject.getJSONArray("results");

            if(null != reviewsJsonArray && reviewsJsonArray.length() != 0) {
                for (int i = 0; i < reviewsJsonArray.length(); i++) {

                    JSONObject reviewObject = reviewsJsonArray.getJSONObject(i);

                    String author = reviewObject.optString("author");
                    String text = reviewObject.optString("content");

                    Review review = new Review(author, text, "");

                    reviewList.add(review);
                }
            }
            else {
                Log.d("JSON REVIEW ARRAY","JSON REVIEWS ARRAY IS EMPTY OR NULL");
            }
            return reviewList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }
}

