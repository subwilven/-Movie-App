package com.example.android.movieapp.movieDetails.interactor;

import com.example.android.movieapp.FetchReviewsData;
import com.example.android.movieapp.FetchVideosData;
import com.example.android.movieapp.POJO.Review;
import com.example.android.movieapp.POJO.Video;

import java.util.List;

/**
 * Created by eslam on 24-Feb-18.
 */

public class DetailsInteractor {

    public interface CallBack {
        void onReviewsReceived(List<Review> reviews);

        void onVideosReceived(List<Video> videos);
    }

    public void loadVideosData(String id, final CallBack listener) {
        FetchVideosData.HandleResults handleVideosResults = new FetchVideosData.HandleResults() {
            @Override
            public void showNoResultTestView() {

            }

            @Override
            public void scrollToPosition() {
            }

            @Override
            public void updateTheAdapter(String[] strings) {
                listener.onVideosReceived(null);
            }

            @Override
            public void showProgressBar() {
            }

            @Override
            public void hideProgressBar() {

            }
        };

        FetchVideosData fetchVideosData = new FetchVideosData(handleVideosResults);
        fetchVideosData.execute(id);
    }

    public void loadReviewsData(String id, final CallBack listener) {
        FetchReviewsData.HandleResults handleReviewsResults = new FetchReviewsData.HandleResults() {
            @Override
            public void showNoResultTestView() {

            }

            @Override
            public void updateTheAdapter(String[] strings) {
                listener.onReviewsReceived(null);
            }

            @Override
            public void scrollToPosition() {

            }

            @Override
            public void showProgressBar() {
            }

            @Override
            public void hideProgressBar() {
            }
        };

        FetchReviewsData fetchReviewsData = new FetchReviewsData(handleReviewsResults);
        fetchReviewsData.execute(id);
    }
}