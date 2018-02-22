package com.example.android.movieapp.POJO;

import java.io.Serializable;

/**
 * Created by eslam on 21-Feb-18.
 */

public class Movie implements Serializable{
    private String id;
    private String overview;
    private String original_title;
    private String poster_path;
    private String vote_average;
    private String release_date;

    public void setId(String id) {
        this.id = id;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

      public String getId()
    {
        return id;
    }
    public String getOverview()
    {
        return overview;
    }

    public String getOriginal_title()
    {
        return original_title;
    }
    public String getPoster_path()
    {
        return poster_path;
    }
    public String getVote_average()
    {
        return vote_average;
    }
    public String getRelease_date()
    {
        return release_date;
    }




}
