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
import Models.Movie;

class JsonUtils {

    private final static String RATING = "vote_average";
    private final static String TITLE = "title";
    private final static String PLOT = "overview";
    private final static String RELEASE_DATE =  "release_date";

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
                    String imageUrl = movieObject.optString("poster_path");


                    Movie movie = new Movie(imageUrl, title, rating, plot, release_date);

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

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }
}
