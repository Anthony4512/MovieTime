package com.amirely.elite.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import Models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private final static String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185_and_h278_bestv2";

    public interface MovieListClickListener {
        void onMovieClick(int movieClickedIndex);
    }

    private final List<Movie> mMovieList;
    final private MovieListClickListener mMovieClickListener;


    public MovieAdapter(List<Movie> movieList, MovieListClickListener movieListClickListener) {
        this.mMovieList = movieList;
        mMovieClickListener = movieListClickListener;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idForMovieListItem = R.layout.movie_list_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(idForMovieListItem, parent, false);

        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {

        Movie movie = mMovieList.get(position);
        Uri movieUri = Uri.parse(BASE_IMAGE_URL + movie.getPosterString());
        Context context = holder.imageViewIcon.getContext();

        holder.title.setText(movie.getTitle());
        Picasso.with(context).load(movieUri).into(holder.imageViewIcon);
    }

    @Override
    public int getItemCount() {
        if(null != this.mMovieList) {
            return this.mMovieList.size();
        }
        else {
            return 0;
        }
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView title;
        final ImageView imageViewIcon;

        MovieHolder(View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.movie_thumbnail_tv);
            title = itemView.findViewById(R.id.movie_title_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mMovieClickListener.onMovieClick(clickedPosition);
        }
    }
}
