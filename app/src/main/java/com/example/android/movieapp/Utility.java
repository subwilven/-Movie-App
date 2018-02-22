package com.example.android.movieapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by eslam on 03-Oct-17.
 */

public class Utility {
    public final static String basicUrl = "http://api.themoviedb.org/3/movie/";
    private final static String api = "api_key";

    public static boolean haveNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static URL getMoviesUrl(String sortType)
    {

        Uri builtUri = Uri.parse(basicUrl).buildUpon()
                .appendEncodedPath(sortType)
                .appendQueryParameter(api, BuildConfig.Movie_Database_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static URL getReviewsUrl(String id)
    {
        Uri buildUri = Uri.parse(basicUrl).buildUpon()
                .appendPath(id)
                .appendPath("reviews")
                .appendQueryParameter(api, BuildConfig.Movie_Database_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static URL getVideosUrl(String id)
    {
        Uri buildUri = Uri.parse(basicUrl).buildUpon()
                .appendPath(id)
                .appendPath("videos")
                .appendQueryParameter(api, BuildConfig.Movie_Database_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    @Nullable
    public static String getResponseFromHttpUrl(URL url) throws IOException {


        HttpURLConnection urlConnection = (HttpURLConnection) (url != null ? url.openConnection() : null);
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);
        try {
            InputStream in = urlConnection != null ? urlConnection.getInputStream() : null;

            Scanner scanner = new Scanner(in != null ? in : null);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
    }

}
