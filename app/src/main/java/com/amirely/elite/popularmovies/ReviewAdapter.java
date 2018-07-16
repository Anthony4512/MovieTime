package com.amirely.elite.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import models.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private final List<Review> mReviewList;


    ReviewAdapter(List<Review> reviewList) {
        this.mReviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idForReviewListItem = R.layout.review_list_view;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(idForReviewListItem, parent, false);

        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Review review = mReviewList.get(position);

        holder.author.setText(review.getAuthor());
//        holder.date.setText(review.getDate());
        holder.text.setText(review.getText());
    }

    @Override
    public int getItemCount() {
        if(null != this.mReviewList) {
            return this.mReviewList.size();
        }
        else {
            return 0;
        }
    }

    class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView author;
        final TextView date;
        final TextView text;

        ReviewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author_tv);
            date = itemView.findViewById(R.id.review_date_tv);
            text = itemView.findViewById(R.id.review_tv);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
//            mMovieClickListener.onMovieClick(clickedPosition);
        }
    }


}
