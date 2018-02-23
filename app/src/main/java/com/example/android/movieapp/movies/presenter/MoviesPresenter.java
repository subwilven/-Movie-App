package com.example.android.movieapp.movies.presenter;

import android.database.Cursor;

import com.example.android.movieapp.POJO.Movie;
import com.example.android.movieapp.movies.interactor.MoviesInteractor;
import com.example.android.movieapp.movies.view.AdatperView;
import com.example.android.movieapp.movies.view.MoviesView;

import java.util.List;

/**
 * Created by eslam on 23-Feb-18.
 */

public class MoviesPresenter implements MoviesInteractor.CallBack{

    private final MoviesInteractor mInteractor;
    private final MoviesView mView;
    private final AdatperView mAdapter ;

    public MoviesPresenter(MoviesView view, AdatperView adatperView,MoviesInteractor interactor)
    {
        mView=view;
        mInteractor=interactor;
        mAdapter=adatperView;
    }

    public void loadData()
    {
        if(!mView.checkInternetConnection())
        {
            mView.setNoConnection(false);
            return;
        }
        mView.showProgress();
        String sortType=mView.getSortType();
        if(sortType.equals("favorite"))
        {
            mView.loadDataFromLoader();
        }
        else
        {
            mInteractor.loadMoviesFromApi(sortType,this);
        }

    }

    @Override
    public void onDataReceived(List<Movie> movies) {
        mAdapter.sendData(movies);
    }

    public void onDataReceived(Cursor movies) {
        mAdapter.sendData(movies);
    }
}
