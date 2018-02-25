package com.example.android.movieapp.POJO;

import java.util.List;

/**
 * Created by eslam on 25-Feb-18.
 */

public class ReviewResults {
    private List<Review> results;
    public List<Review> getResults()
    {
        return results;
    }
    public void setResults(List<Review> list)
    {
        results=list;
    }
}
