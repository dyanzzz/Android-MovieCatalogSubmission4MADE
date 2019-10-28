package com.submission.moviecatalogsubmission4made.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowResponse {
    @SerializedName("results")
    private List<TvShow> tvShow;

    public List<TvShow> getTvShow() {
        return tvShow;
    }

    public void setTvShow(List<TvShow> tvShow) {
        this.tvShow = tvShow;
    }
}
