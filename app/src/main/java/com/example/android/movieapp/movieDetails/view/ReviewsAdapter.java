package com.example.android.movieapp.movieDetails.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapp.POJO.Review;
import com.example.android.movieapp.R;

import java.util.List;

/**
 * Created by eslam on 15-Oct-17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> implements ReviewsAdapterView{

    private List<Review>  mData;

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutID = R.layout.list_item_review;
        View view = inflater.inflate(layoutID, parent, false);
        return new ReviewHolder(view);

    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {

        Review reviewData = mData.get(position);
        holder.setAuthorTextView(reviewData.getAuthor());
        holder.setContentTextView(reviewData.getContent());
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else
            return 0;

    }

    @Override
    public void sendData(List<Review> reviews) {
        mData=reviews;
        notifyDataSetChanged();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{

        final TextView authorTextView;
        final TextView contentTextView;

        public ReviewHolder(View itemView) {
            super(itemView);
            authorTextView = (TextView) itemView.findViewById(R.id.review_author);
            contentTextView = (TextView) itemView.findViewById(R.id.review_content);

        }

        public void setAuthorTextView(String text) {
            authorTextView.setText(text);
        }

        public void setContentTextView(String text) {
            contentTextView.setText(text);
        }

    }
}
