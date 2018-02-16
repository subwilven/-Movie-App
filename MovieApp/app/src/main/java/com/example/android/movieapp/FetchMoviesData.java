package com.example.android.movieapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by eslam on 01-Oct-17.
 */

@SuppressWarnings("DefaultFileTemplate")
class FetchMoviesData extends AsyncTask<String, Void, String[]> {



    // The adapter of the main activity recycler view to update it after getting the result
    private HandleResults mHandleResults;
    public interface HandleResults{
        void udpateAdpaterData(String[] strings);
        void scrollToPosition();
    }
    private String[] result;

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        mHandleResults.udpateAdpaterData(strings);
        mHandleResults.scrollToPosition();
    }

    public FetchMoviesData(HandleResults handleResults) {
        mHandleResults = handleResults;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        String jasonString;
        String sortType = strings[0];

        try {
            URL url = Utility.getMoviesUrl(sortType);
            jasonString=Utility.getResponseFromHttpUrl(url);
            result = load(jasonString);

            // Log.i("kkkk", jasonString);
            return result;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String[] load(String jasonString) throws JSONException {
        final String RESULTS = "results";
        final String ID = "id";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String RELEASE_DATE = "release_date";

        String[] moviesDataArray;
        JSONObject jsonObject = new JSONObject(jasonString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);

        moviesDataArray = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            String id;
            String overview;
            String title;
            String posterPath;
            String voteAverage;
            String releaseDate;


            JSONObject movieObject = jsonArray.getJSONObject(i);
            id = movieObject.getString(ID);
            overview = movieObject.getString(OVERVIEW);
            title = movieObject.getString(ORIGINAL_TITLE);
            voteAverage = movieObject.getString(VOTE_AVERAGE);
            releaseDate = movieObject.getString(RELEASE_DATE);
            posterPath = movieObject.getString(POSTER_PATH);
            posterPath = posterPath.replace("/", "");

            moviesDataArray[i] = title + "&&&"
                    + overview + "&&&"
                    + posterPath + "&&&"
                    + voteAverage + "&&&"
                    + releaseDate + "&&&"
                    + id;
        }

        return moviesDataArray;
    }


}
