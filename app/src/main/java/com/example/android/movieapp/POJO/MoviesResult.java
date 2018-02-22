package com.example.android.movieapp.POJO;

import java.util.List;

/**
 * Created by eslam on 22-Feb-18.
 */

public class MoviesResult {
    private List<Movie> results;
    public List<Movie> getResults()
    {
        return results;
    }
    public void setResults(List<Movie> list)
    {
        results=list;
    }
}
