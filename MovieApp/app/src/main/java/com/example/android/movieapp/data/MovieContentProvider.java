package com.example.android.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by eslam on 14-Oct-17.
 */

public class MovieContentProvider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();
    private static MovieDpHelper movieDpHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieAppContract.CONTENT_AUTHORITY, MovieAppContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(MovieAppContract.CONTENT_AUTHORITY, MovieAppContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDpHelper = new MovieDpHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor resCursor;
        SQLiteDatabase db=movieDpHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {

            case MOVIE: {
                resCursor=db.query(MovieAppContract.MovieEntry.TABLE_NAME
                        ,columns
                        ,selection
                        ,selectionArgs
                        ,null
                        ,null
                        ,sortOrder);

                break;
            }
            case MOVIE_WITH_ID: {
                String slc = MovieAppContract.MovieEntry._ID+" = ?";
                String id = MovieAppContract.MovieEntry.getIDFromUri(uri);
                resCursor=db.query(MovieAppContract.MovieEntry.TABLE_NAME
                        ,columns
                        ,slc
                        ,new String[]{id}
                        ,null
                        ,null
                        ,null);
                break;
            }

            default:
                throw new UnsupportedOperationException("unKnown Uri :" + uri.toString());

        }
        resCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return resCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase dp =movieDpHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Uri resUri;
        switch (match)
        {
            case MOVIE:
            {
                long id=dp.insert(MovieAppContract.MovieEntry.TABLE_NAME,null,contentValues);
                resUri= MovieAppContract.MovieEntry.buildMovieWithIDUri(Long.toString(id));
                break;
            }
            default:
                throw new UnsupportedOperationException("unknown Uri :"+uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return resUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase dp =movieDpHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        int id;
        switch (match)
        {
            case MOVIE:
            {
                id=dp.delete(MovieAppContract.MovieEntry.TABLE_NAME,null,null);
                break;
            }
            case MOVIE_WITH_ID:
            {
                String whereClause= MovieAppContract.MovieEntry._ID+" =?";
                String movieID= MovieAppContract.MovieEntry.getIDFromUri(uri);
                id=dp.delete(MovieAppContract.MovieEntry.TABLE_NAME,whereClause,new String[]{movieID});
                break;
            }
            default:
                throw new UnsupportedOperationException("unknown Uri :"+uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
