package com.example.android.movieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by eslam on 14-Oct-17.
 */

public class MovieAppContract  {

    public static final String CONTENT_AUTHORITY ="com.example.android.movieapp";
    public static final String PATH_MOVIE="movie";

    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME="movie";
        public static final String COL_MOVIE_TITLE="title";
        public static final String COL_MOVIE_VOTE_AVERAGE="vote_average";
        public static final String COL_MOVIE_RELEASE_DATE="release_date";
        public static final String COL_MOVIE_OVERVIEW="overview";
        public static final String COL_MOVIE_POSTER="poster";

        public static Uri buildMovieWithIDUri(String id)
        {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
        public static String getIDFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }
    }







}
