package com.example.android.movieapp.movies.view;

/**
 * Created by eslam on 23-Feb-18.
 */

public interface MoviesView {
    void showProgress();
    void hideProgress();
    boolean checkInternetConnection();
    void setNoConnection(boolean thereIsConnection);
    String getSortType();
    void loadDataFromLoader();

}
