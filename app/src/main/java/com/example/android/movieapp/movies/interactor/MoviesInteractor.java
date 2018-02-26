package com.example.android.movieapp.movies.interactor;

import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.POJO.Movie;
import com.example.android.movieapp.POJO.MoviesResult;
import com.example.android.movieapp.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by eslam on 22-Feb-18.
 */

public class MoviesInteractor {


    public interface CallBack {
        void onDataReceived(List<Movie> movies);
    }

    public interface MoviesApi {
        @GET("{path}")
        Call<MoviesResult> getMovies(@Path(value = "path", encoded = true) String path2,
                                     @Query("api_key") String key);
    }

    public void loadMoviesFromApi(String sortType, final CallBack listener) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.basicUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesInteractor.MoviesApi moviesApi = retrofit.create(MoviesInteractor.MoviesApi.class);
        Call<MoviesResult> connection = moviesApi.getMovies(
                sortType,
                BuildConfig.Movie_Database_API_KEY);

        connection.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                listener.onDataReceived(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                int x = 5;
            }
        });

    }


}
