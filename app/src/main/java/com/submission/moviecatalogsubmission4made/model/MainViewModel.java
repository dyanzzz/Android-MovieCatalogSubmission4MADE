package com.submission.moviecatalogsubmission4made.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.submission.moviecatalogsubmission4made.repository.Repository;

public class MainViewModel extends ViewModel {

    private MutableLiveData<MovieResponse> movieData;
    private MutableLiveData<TvShowResponse> tvShowData;

    public void init() {
        Repository repository;
        repository = Repository.getInstance();
        if (movieData == null) {
            movieData = repository.getMovies();
        }

        if (tvShowData == null) {
            tvShowData = repository.getTvShows();
        }
    }

    public LiveData<MovieResponse> getMovies() {
        return movieData;
    }

    public LiveData<TvShowResponse> getTvShows() {
        return tvShowData;
    }


}
