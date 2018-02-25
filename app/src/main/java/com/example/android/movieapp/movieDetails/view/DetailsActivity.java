package com.example.android.movieapp.movieDetails.view;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.POJO.Movie;
import com.example.android.movieapp.R;
import com.example.android.movieapp.Utility;
import com.example.android.movieapp.data.MovieAppContract;
import com.example.android.movieapp.data.MovieAppContract.MovieEntry;
import com.example.android.movieapp.movieDetails.interactor.DetailsInteractor;
import com.example.android.movieapp.movieDetails.presenter.DetailsPresenter;
import com.squareup.picasso.Picasso;

/**
 * Created by eslam on 02-Oct-17.
 */

@SuppressWarnings("ALL")
public class DetailsActivity extends AppCompatActivity implements VideosAdapter.OnVideoClicked, DetailsView {

    private final String BUNDLE_REVIEWS_RECYCLER_POSITION = "reivews_recycler_position";
    private final String BUNDLE_VIDEOS_RECYCLER_POSITION = "videos_recycler_position";
    private Movie movie;
    private TextView overviewTextView;
    private TextView titleTextView;
    private ImageView posterImageView;
    private TextView releaseDateTextView;
    private TextView voteAverageTextView;
    // private Button addToFavoriteButton;

    private boolean isFavorite;

    boolean videosReady = false;
    boolean reviewsReady = false;

    ProgressBar videoProgressBar;
    ProgressBar reviewsProgressBar;

    ScrollView mScrollView;
    TextView noVideosResult;
    TextView noReviwsResult;

    ReviewsAdapter reviewsAdapter;
    VideosAdapter videoAdapter;
    RecyclerView reviewRecyclerView;

    RecyclerView videoRecyclerView;

    Bundle mSavedInstanceState;

    private  DetailsPresenter presenter;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
    }

    public void scrollScrollViewToPosition() {
        if (videosReady && reviewsReady) {
            final int[] position = mSavedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
            if (position != null)
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(position[0], position[1]);
                    }
                });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("data");
        mSavedInstanceState = savedInstanceState;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        overviewTextView = (TextView) findViewById(R.id.details_movie_overview);
        releaseDateTextView = (TextView) findViewById(R.id.details_movie_release_date);
        voteAverageTextView = (TextView) findViewById(R.id.details_movie_vote_average);
        posterImageView = (ImageView) findViewById(R.id.details_movie_poster);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
        videoRecyclerView = (RecyclerView) findViewById(R.id.video_recycler_view);
        videoProgressBar = (ProgressBar) findViewById(R.id.pb_loading_videos);
        reviewsProgressBar = (ProgressBar) findViewById(R.id.pb_loading_reviews);
        noVideosResult = (TextView) findViewById(R.id.tv_no_videos_result);
        noReviwsResult = (TextView) findViewById(R.id.tv_no_reviews_result);
        mScrollView = (ScrollView) findViewById(R.id.sv_details);
        reviewsAdapter = new ReviewsAdapter();
        videoAdapter = new VideosAdapter(this);

        presenter = new DetailsPresenter(this,videoAdapter,reviewsAdapter,new DetailsInteractor());

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView.addItemDecoration(mDividerItemDecoration);
        reviewRecyclerView.setAdapter(reviewsAdapter);
        //reviewRecyclerView.setFocusable(false);
        reviewRecyclerView.setNestedScrollingEnabled(false);

        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoRecyclerView.setAdapter(videoAdapter);
        videoRecyclerView.setNestedScrollingEnabled(false);
        videoRecyclerView.addItemDecoration(mDividerItemDecoration);
        presenter.loadReviews(movie.getId());
        presenter.loadVideos(movie.getId());
        overviewTextView.setText(movie.getOverview());
        getSupportActionBar().setTitle(movie.getOriginal_title());
        releaseDateTextView.setText(movie.getRelease_date());
        voteAverageTextView.setText(movie.getVote_average());
        movie.setBackdrop_path(movie.getBackdrop_path().replace("/", ""));
        Picasso.with(this).load(buildImageUrl(movie.getBackdrop_path())).into(posterImageView);
        checkIfFavorite();
    }

    private String buildImageUrl(String posterPath) {
        String BASIC_IMAGE_URL = "http://image.tmdb.org/t/p/";
        String IMAGE_SIZE = "w342";
        Uri builderUri = Uri.parse(BASIC_IMAGE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendPath(posterPath)
                .build();

        return builderUri.toString();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Toast.makeText(this, "hahaha", Toast.LENGTH_SHORT).show();
        if (id == R.id.action_favorite) {
            if (!isFavorite) {//add the movie to the database within a abockground thread
                AsyncTask addMovieToDataBase = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        ContentValues movieValues = new ContentValues();
                        movieValues.put(MovieEntry._ID, movie.getId());
                        movieValues.put(MovieEntry.COL_MOVIE_OVERVIEW, movie.getOverview());
                        movieValues.put(MovieEntry.COL_MOVIE_RELEASE_DATE, movie.getRelease_date());
                        movieValues.put(MovieEntry.COL_MOVIE_TITLE, movie.getOriginal_title());
                        movieValues.put(MovieEntry.COL_MOVIE_VOTE_AVERAGE, movie.getVote_average());
                        movieValues.put(MovieEntry.COL_MOVIE_POSTER, movie.getPoster_path());
                        getContentResolver().insert(MovieEntry.CONTENT_URI, movieValues);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        markAsFavorite();
                        Toast.makeText(DetailsActivity.this, "Added to Favorite", Toast.LENGTH_SHORT).show();
                    }
                };
                addMovieToDataBase.execute();
            } else {//delete the movie from the database within a abockground thread
                AsyncTask deleteMoviefromDataBase = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        getContentResolver().delete(MovieAppContract.MovieEntry.buildMovieWithIDUri(movie.getId()), null, null);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        markAsNotFavroite();
                        Toast.makeText(DetailsActivity.this, "removed from Favorite", Toast.LENGTH_SHORT).show();
                    }
                };
                deleteMoviefromDataBase.execute();
            }
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void checkIfFavorite() {
        AsyncTask checkIfMovieIsFavroite = new AsyncTask<Object, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Object... voids) {
                Cursor cursor = getContentResolver().query(MovieEntry.buildMovieWithIDUri(movie.getId())
                        , new String[]{MovieEntry._ID}
                        , null
                        , null
                        , null);
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                if (cursor.getCount() > 0) {
                    markAsFavorite();
                } else {
                    markAsNotFavroite();
                }
            }
        };
        checkIfMovieIsFavroite.execute();
    }

    @Override
    public void handleOnClickVideo(String key) {
        boolean isAppInstalled = appInstalledOrNot("com.google.android.youtube");
        if (isAppInstalled) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
            intent.putExtra("VIDEO_ID", key);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Youtube Is Not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    //to check if the user has the youtuvbe app installed
    private boolean appInstalledOrNot(String uri) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_favorite);
        if (isFavorite) {
            item.setIcon(android.R.drawable.star_big_on);
        } else {
            item.setIcon(android.R.drawable.star_big_off);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void markAsFavorite() {
        invalidateOptionsMenu();
        isFavorite = true;
    }

    private void markAsNotFavroite() {
        invalidateOptionsMenu();
        isFavorite = false;
    }

    @Override
    public void showVideosProgress() {
        videoProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideVideosProgress() {
        videoProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showReviewsProgress() {
        reviewsProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReviewsProgress() {
        reviewsProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNoVideosFound() {
        noVideosResult.setVisibility(View.VISIBLE);
        noVideosResult.setText(getString(R.string.no_reviews_results));
    }

    @Override
    public void showNoReviewsFound() {
        noReviwsResult.setVisibility(View.VISIBLE);
        noReviwsResult.setText(getString(R.string.no_reviews_results));
    }

    @Override
    public boolean checkInternetConnection() {
        return Utility.haveNetworkConnection(this);
    }

    @Override
    public void setNoConnection(boolean thereIsConnection) {
       if(!thereIsConnection)
       {
           noReviwsResult.setVisibility(View.VISIBLE);
           noVideosResult.setVisibility(View.VISIBLE);
           noReviwsResult.setText(getString(R.string.no_connection));
           noVideosResult.setText(getString(R.string.no_connection));
       }
       else
       {
           noReviwsResult.setVisibility(View.INVISIBLE);
           noVideosResult.setVisibility(View.INVISIBLE);
       }
    }
}
