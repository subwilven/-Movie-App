package com.example.android.movieapp.movies.view;

import android.database.Cursor;

import com.example.android.movieapp.POJO.Movie;

import java.util.List;

/**
 * Created by eslam on 24-Feb-18.
 */

public interface AdatperView {
    void sendData(List<Movie> movies);
    void sendData(Cursor movies);
}
