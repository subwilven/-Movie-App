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

public class FetchVideosData extends AsyncTask<String, Void, String[]> {

//    VideosAdapter mVideosAdapter;
//    ProgressBar mVideosProgressBar;

    HandleResults mHandleResults;
    public interface HandleResults{
        void showNoResultTestView();
        void updateTheAdapter(String[] strings);
        void showProgressBar();
        void hideProgressBar();
        void scrollToPosition();
    }
    public FetchVideosData(HandleResults handleResults) {
        mHandleResults = handleResults;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
      mHandleResults.showProgressBar();
    }

    @Override
    protected String[] doInBackground(String... strings) {
        String jasonString;
        String id = strings[0];

        try {
            URL url = Utility.getVideosUrl(id);
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
        final String KEY = "key";
        final String VIDEO_ID = "id";
        final String NAME = "name";
        final String SIZE = "size";
        final String TYPE = "type";

        String videoID;
        String name;
        String size;
        String type;
        String key;

        JSONObject jsonObject = new JSONObject(jasonString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
        String[] result = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            String videoData;
            JSONObject movie = jsonArray.getJSONObject(i);
            videoID = movie.getString(VIDEO_ID);
            name = movie.getString(NAME);
            size = movie.getString(SIZE);
            type = movie.getString(TYPE);
            key = movie.getString(KEY);

            videoData = videoID + "&&&"
                    + name + "&&&"
                    + size + "&&&"
                    + type + "&&&"
                    + key + "&&&";
            result[i] = videoData;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
//        mVideosProgressBar.setVisibility(View.GONE);
//        mVideosAdapter.setAdapterData(strings);
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
