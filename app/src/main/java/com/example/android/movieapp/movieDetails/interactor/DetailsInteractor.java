package com.example.android.movieapp.movieDetails.interactor;

import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.POJO.Review;
import com.example.android.movieapp.POJO.ReviewResults;
import com.example.android.movieapp.POJO.Video;
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
 * Created by eslam on 24-Feb-18.
 */

public class DetailsInteractor {

    public interface ReviewsApi {
        @GET("{id}/reviews")
        Call<ReviewResults> getReviews(@Path(value = "id", encoded = false) String id,
                                       @Query("api_key") String key);
    }

    public interface CallBack {
        void onReviewsReceived(List<Review> reviews);
        void onVideosReceived(List<Video> videos);
    }

    public void loadVideosData(String id, final CallBack listener) {


    }

    public void loadReviewsData(String id, final CallBack listener) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.basicUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ReviewsApi reviewsApi = retrofit.create(ReviewsApi.class);
        Call<ReviewResults> connection = reviewsApi.getReviews(id, BuildConfig.Movie_Database_API_KEY);

        connection.enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                listener.onReviewsReceived(response.body().getResults());
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {

            }
        });
    }
}