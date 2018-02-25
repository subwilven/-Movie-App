package com.example.android.movieapp.movieDetails.view;

/**
 * Created by eslam on 24-Feb-18.
 */

public interface DetailsView {
    void showVideosProgress();
    void hideVideosProgress();
    void showReviewsProgress();
    void hideReviewsProgress();
    void showNoVideosFound();
    void showNoReviewsFound();
    boolean checkInternetConnection();
    void setNoConnection(boolean thereIsConnection);
}
