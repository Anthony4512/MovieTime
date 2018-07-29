package com.amirely.elite.popularmovies;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import com.amirely.elite.popularmovies.utils.ApiKeyProvider;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import models.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> movieList;
    private String MOVIES_URL;
    private String SORT_BY;

    public MainActivityViewModel() {
        SORT_BY = "popular";
        MOVIES_URL = "https://api.themoviedb.org/3/movie/" + SORT_BY + "?api_key=" + ApiKeyProvider.API_KEY + "&language=en-US&page=1";
    }

    public LiveData<List<Movie>> getMovieList(String sortBy) {
        if(this.movieList == null) {
            movieList = new MutableLiveData<>();
            if(sortBy.equals("favorites")) {
                System.out.println("It's equals favorites");
            }
            else {
                loadMovies(MOVIES_URL);
            }
        }
        return this.movieList;
    }

    private void loadMovies(String movieUrl) {
        new MovieFetcher().execute(movieUrl);
    }

    public void setSORT_BY(String SORT_BY) {
        this.SORT_BY = SORT_BY;
    }

    public void changeSortBy(String sortBy) {
        String moviesUrl = "https://api.themoviedb.org/3/movie/" + sortBy + "?api_key=" + ApiKeyProvider.API_KEY + "&language=en-US&page=1";
        loadMovies(moviesUrl);
    }

    public String getSORT_BY() {
        return SORT_BY;
    }

    @SuppressLint("StaticFieldLeak")
    class MovieFetcher extends AsyncTask<String, Void, String> {

        final OkHttpClient client = new OkHttpClient();
        MutableLiveData<List<Movie>> tempMovieList;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String results = run(strings[0]);
                tempMovieList = JsonUtils.parseMoviesJson(results);
                return "";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            movieList.postValue(tempMovieList.getValue());
        }

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        }
    }
}
