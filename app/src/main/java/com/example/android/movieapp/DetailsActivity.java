package com.example.android.movieapp;

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

import com.example.android.movieapp.data.MovieAppContract;
import com.example.android.movieapp.data.MovieAppContract.MovieEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by eslam on 02-Oct-17.
 */

@SuppressWarnings("ALL")
public class DetailsActivity extends AppCompatActivity implements VideosAdapter.OnVideoClicked {

    private final String BUNDLE_REVIEWS_RECYCLER_POSITION = "reivews_recycler_position";
    private final String BUNDLE_VIDEOS_RECYCLER_POSITION = "videos_recycler_position";
    private String data;
    private TextView overviewTextView;
    private TextView titleTextView;
    private ImageView posterImageView;
    private TextView releaseDateTextView;
    private TextView voteAverageTextView;
    // private Button addToFavoriteButton;

    private boolean isFavorite;
    String[] movieData;

    boolean videosReady =false;
    boolean reviewsReady=false;

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//   outState.putParcelable(BUNDLE_REVIEWS_RECYCLER_POSITION, reviewRecyclerView.getLayoutManager().onSaveInstanceState());
////        outState.putParcelable(BUNDLE_VIDEOS_RECYCLER_POSITION, videoRecyclerView.getLayoutManager().onSaveInstanceState());
//        int lastFirstVisiblePosition = ((LinearLayoutManager)reviewRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
////        int lastVideoVisiblePosition = ((LinearLayoutManager)videoRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//        outState.putInt(BUNDLE_REVIEWS_RECYCLER_POSITION,lastFirstVisiblePosition);

        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
    }

    public void scrollScrollViewToPosition()
    {
        if(videosReady&&reviewsReady) {
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
        data = intent.getStringExtra("data");
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
        mScrollView =(ScrollView)findViewById(R.id.sv_details);

        reviewsAdapter = new ReviewsAdapter();
        videoAdapter = new VideosAdapter(this);

        movieData = data.split("&&&");

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

        if (Utility.haveNetworkConnection(this)) {
            loadReviwsData();
            loadVideosData();
            noReviwsResult.setVisibility(View.GONE);
            noVideosResult.setVisibility(View.GONE);
        } else {
            noReviwsResult.setVisibility(View.VISIBLE);
            noReviwsResult.setText(getString(R.string.no_connection));
        }

        getSupportActionBar().setTitle(movieData[0]);

        overviewTextView.setText(movieData[1]);
        getSupportActionBar().setTitle(movieData[0]);
        //  titleTextView.setText(movieData[0]);
        releaseDateTextView.setText(movieData[4]);
        voteAverageTextView.setText(movieData[3]);

        Picasso.with(this).load(buildImageUrl(movieData[2])).into(posterImageView);
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
                        movieValues.put(MovieEntry._ID, movieData[5]);
                        movieValues.put(MovieEntry.COL_MOVIE_OVERVIEW, movieData[1]);
                        movieValues.put(MovieEntry.COL_MOVIE_RELEASE_DATE, movieData[4]);
                        movieValues.put(MovieEntry.COL_MOVIE_TITLE, movieData[0]);
                        movieValues.put(MovieEntry.COL_MOVIE_VOTE_AVERAGE, movieData[3]);
                        movieValues.put(MovieEntry.COL_MOVIE_POSTER, movieData[2]);
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
                        getContentResolver().delete(MovieAppContract.MovieEntry.buildMovieWithIDUri(movieData[5]), null, null);
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
                Cursor cursor = getContentResolver().query(MovieEntry.buildMovieWithIDUri(movieData[5])
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

    public void loadReviwsData() {
        FetchReviewsData.HandleResults handleReviewsResults = new FetchReviewsData.HandleResults() {
            @Override
            public void showNoResultTestView() {
                noReviwsResult.setVisibility(View.VISIBLE);
                noReviwsResult.setText(getString(R.string.no_reviews_results));
            }

            @Override
            public void updateTheAdapter(String[] strings) {
                reviewsAdapter.setAdapterData(strings);
            }

            @Override
            public void scrollToPosition() {
                if (mSavedInstanceState != null) {
//                    Parcelable savedRecyclerLayoutState = mSavedInstanceState.getParcelable(BUNDLE_REVIEWS_RECYCLER_POSITION);
//                    reviewRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
//
//                    final int lastFirstVisiblePosition =mSavedInstanceState.getInt(BUNDLE_REVIEWS_RECYCLER_POSITION);
//                    reviewRecyclerView.scrollToPosition(lastFirstVisiblePosition);
                    reviewsReady=true;
                    scrollScrollViewToPosition();

                }
            }

            @Override
            public void showProgressBar() {
                reviewsProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideProgressBar() {
                reviewsProgressBar.setVisibility(View.GONE);
            }
        };

        FetchReviewsData fetchReviewsData = new FetchReviewsData(handleReviewsResults);
        fetchReviewsData.execute(movieData[5]);

    }

    public void loadVideosData() {
        FetchVideosData.HandleResults handleVideosResults = new FetchVideosData.HandleResults() {
            @Override
            public void showNoResultTestView() {
                noVideosResult.setVisibility(View.VISIBLE);
                noVideosResult.setText(getString(R.string.no_video_results));
            }

            @Override
            public void scrollToPosition() {
                if(mSavedInstanceState!=null)
                {
                    videosReady=true;
                    scrollScrollViewToPosition();
                }
            }

            @Override
            public void updateTheAdapter(String[] strings) {
                videoAdapter.setAdapterData(strings);
            }

            @Override
            public void showProgressBar() {
                videoProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideProgressBar() {
                videoProgressBar.setVisibility(View.GONE);
            }
        };

        FetchVideosData fetchVideosData = new FetchVideosData(handleVideosResults);
        fetchVideosData.execute(movieData[5]);
    }


}
