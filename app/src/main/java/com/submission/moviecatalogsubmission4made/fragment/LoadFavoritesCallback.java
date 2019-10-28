package com.submission.moviecatalogsubmission4made.fragment;

import com.submission.moviecatalogsubmission4made.model.Movie;
import com.submission.moviecatalogsubmission4made.model.TvShow;

import java.util.List;

public interface LoadFavoritesCallback {
    void preExecute();
    void postExecuteMovie(List<Movie> movies);
    void postExecuteTvShow(List<TvShow> tvShows);
}
