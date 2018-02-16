package com.example.android.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.movieapp.data.MovieAppContract.MovieEntry;

/**
 * Created by eslam on 14-Oct-17.
 */

public class MovieDpHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie_app.db";
    private static final int DATABASE_VERSION = 2;

    public MovieDpHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COL_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COL_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieEntry.COL_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COL_MOVIE_POSTER + " TEXT , " +
                MovieEntry.COL_MOVIE_OVERVIEW + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
