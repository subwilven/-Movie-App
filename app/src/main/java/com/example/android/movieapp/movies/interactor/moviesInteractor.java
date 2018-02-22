package com.example.android.movieapp.movies.interactor;

import com.example.android.movieapp.POJO.MoviesResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by eslam on 22-Feb-18.
 */

public class moviesInteractor {


    public interface MoviesApi {
        @GET("{path}")
        Call<MoviesResult> getMovies(@Path(value = "path", encoded = true) String path2,
                                     @Query("api_key") String key);
    }
}
