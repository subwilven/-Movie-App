package com.example.android.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.android.movieapp.data.MovieAppContract;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener
        , LoaderManager.LoaderCallbacks<Cursor> {

    private final String BUNDLE_RECYCLER_POSITION = "recycler_position";
    private static final int LOADER_ID = 1234;
    private String sortType;
    private SharedPreferences prefs;
    private Button refreshButton;
    private RecyclerView movieRecyclerView;
    private MoviesAdapter moviesAdapter;
    LinearLayout linearLayout;

    LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;

    public static final String[] MOVIE_COLUMNS =
            new String[]{
                    MovieAppContract.MovieEntry._ID
                    , MovieAppContract.MovieEntry.COL_MOVIE_TITLE
                    , MovieAppContract.MovieEntry.COL_MOVIE_POSTER
                    , MovieAppContract.MovieEntry.COL_MOVIE_VOTE_AVERAGE
                    , MovieAppContract.MovieEntry.COL_MOVIE_OVERVIEW
                    , MovieAppContract.MovieEntry.COL_MOVIE_RELEASE_DATE};


    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_POSTER = 2;
    public static final int COL_VOTE_AVERAGE = 3;
    public static final int COL_OVERVIEW = 4;
    public static final int COL_RELEASE_DATE = 5;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int lastFirstVisiblePosition = ((GridLayoutManager) movieRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(BUNDLE_RECYCLER_POSITION, lastFirstVisiblePosition);
    }

    private void loadData() {
        sortType = prefs.getString("sort_type", getString(R.string.sort_type_default));
        if (sortType.equals(getResources().getString(R.string.favorite))) {
            if (getSupportLoaderManager().getLoader(LOADER_ID) != null) {

                getSupportLoaderManager().restartLoader(LOADER_ID, null, callback);
            } else {
                getSupportLoaderManager().initLoader(LOADER_ID, null, callback);
            }
        } else {
            if (Utility.haveNetworkConnection(this)) {
                movieRecyclerView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
                FetchMoviesData fetchMoviesData = new FetchMoviesData(new FetchMoviesData.HandleResults() {
                    @Override
                    public void udpateAdpaterData(String[] strings) {
                        moviesAdapter.setAdapterData(strings);
                    }

                    @Override
                    public void scrollToPosition() {
                        if (mSavedInstanceState != null) {
                            int lastFirstVisiblePosition = mSavedInstanceState.getInt(BUNDLE_RECYCLER_POSITION);
                            movieRecyclerView.scrollToPosition(lastFirstVisiblePosition);
                        }
                    }
                });
                fetchMoviesData.execute(sortType);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                movieRecyclerView.setVisibility(View.INVISIBLE);
            }
        }
    }

    Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSavedInstanceState = savedInstanceState;
        //======================================================================
        //set up stetho
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(getApplicationContext())
        );
        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
        //======================================================================

        setContentView(R.layout.activity_main);
        moviesAdapter = new MoviesAdapter(this);


        linearLayout = (LinearLayout) findViewById(R.id.no_connection_layout);
        refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sortType = prefs.getString("sort_type", getString(R.string.sort_type_default));


        movieRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        loadData();
        int NUM_OF_COLUMNS = 2;
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_OF_COLUMNS));
        movieRecyclerView.setAdapter(moviesAdapter);

        if (savedInstanceState != null) {
            final int lastFirstVisiblePosition = savedInstanceState.getInt(BUNDLE_RECYCLER_POSITION);
            //using handler the only way that make the scroll work
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    movieRecyclerView.scrollToPosition(lastFirstVisiblePosition);
                }
            }, 200);
//            ((GridLayoutManager)movieRecyclerView.getLayoutManager())
//                    .scrollToPositionWithOffset(lastFirstVisiblePosition,0);
            //       movieRecyclerView.smoothScrollToPosition(lastFirstVisiblePosition);
            //      movieRecyclerView.scrollToPosition(lastFirstVisiblePosition);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemID = item.getItemId();
        if (selectedItemID == R.id.settings_action) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        return new CursorLoader(this, MovieAppContract.MovieEntry.CONTENT_URI
                , MOVIE_COLUMNS
                , null
                , null
                , MovieAppContract.MovieEntry.COL_MOVIE_VOTE_AVERAGE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesAdapter.setAdapterData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
