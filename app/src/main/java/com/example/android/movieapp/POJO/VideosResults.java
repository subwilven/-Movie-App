package com.example.android.movieapp.POJO;

import java.util.List;

/**
 * Created by eslam on 25-Feb-18.
 */

public class VideosResults {
    private List<Video> results;
    public List<Video> getResults()
    {
        return results;
    }
    public void setResults(List<Video> list)
    {
        results=list;
    }
}
