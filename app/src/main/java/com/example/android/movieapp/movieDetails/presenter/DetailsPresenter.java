package com.example.android.movieapp.movieDetails.presenter;

import com.example.android.movieapp.POJO.Review;
import com.example.android.movieapp.POJO.Video;
import com.example.android.movieapp.movieDetails.interactor.DetailsInteractor;
import com.example.android.movieapp.movieDetails.view.DetailsView;
import com.example.android.movieapp.movieDetails.view.ReviewsAdapterView;
import com.example.android.movieapp.movieDetails.view.VideosAdapterView;

import java.util.List;

/**
 * Created by eslam on 24-Feb-18.
 */

public class DetailsPresenter implements DetailsInteractor.CallBack {

    private final DetailsInteractor mInteractor;
    private final DetailsView mView;
    private final VideosAdapterView mVideosAdapterView;
    private final ReviewsAdapterView mReviewsAdapterView;

    public DetailsPresenter(DetailsView view, VideosAdapterView videosAdapterView, ReviewsAdapterView reviewsAdapterView, DetailsInteractor interactor) {
        mInteractor = interactor;
        mVideosAdapterView = videosAdapterView;
        mView = view;
        mReviewsAdapterView = reviewsAdapterView;
    }

    public void loadReviews(String id) {
        if (!mView.checkInternetConnection()) {
            mView.setNoConnection(false);
            return;
        }
        else
        {
            mView.setNoConnection(true);
        }
        mView.showReviewsProgress();
        mInteractor.loadReviewsData(id, this);
    }

    public void loadVideos(String id) {
        if (!mView.checkInternetConnection()) {
            mView.setNoConnection(false);
            return;
        }
        mView.showVideosProgress();
        mInteractor.loadVideosData(id, this);

    }

    @Override
    public void onReviewsReceived(List<Review> reviews) {
        mView.hideReviewsProgress();
        if (reviews != null && reviews.size() > 0) {
            mReviewsAdapterView.sendData(reviews);
        } else {
            mView.showNoReviewsFound();
        }
    }

    @Override
    public void onVideosReceived(List<Video> videos) {
        mView.hideVideosProgress();
        if (videos != null && videos.size() > 0) {
            mVideosAdapterView.sendData(videos);
        } else {
            mView.showNoVideosFound();
        }
    }
}
