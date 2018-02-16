package com.example.android.movieapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by eslam on 15-Oct-17.
 */

public class FetchReviewsData extends AsyncTask<String, Void, String[]> {

    HandleResults mHandleResults;
    public interface HandleResults{
        void showNoResultTestView();
        void updateTheAdapter(String[] strings);
        void showProgressBar();
        void hideProgressBar();
        void scrollToPosition();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mHandleResults.showProgressBar();

    }
    public FetchReviewsData(HandleResults handleResults) {
        mHandleResults = handleResults;
    }
    @Override
    protected String[] doInBackground(String... strings) {
        String jasonString;
        String id = strings[0];

        try {
            URL url = Utility.getReviewsUrl(id);
            jasonString = Utility.getResponseFromHttpUrl(url);
            String[] result = load(jasonString);
            return result;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] load(String jasonString) throws JSONException {

        final String RESULTS = "results";
        final String REVIEW_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        String id;
        String author;
        String content;

        JSONObject jsonObject = new JSONObject(jasonString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
        String[] result = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            String movieData;
            JSONObject movie = jsonArray.getJSONObject(i);
            author = movie.getString(AUTHOR);
            content = movie.getString(CONTENT);

            movieData = author + "&&&" + content;
            result[i] = movieData;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        if(strings.length==0)
        {
            mHandleResults.showNoResultTestView();
            mHandleResults.hideProgressBar();
            mHandleResults.scrollToPosition();
        }
        else
        {
            mHandleResults.updateTheAdapter(strings);
            mHandleResults.hideProgressBar();
            mHandleResults.scrollToPosition();
        }
    }
}
