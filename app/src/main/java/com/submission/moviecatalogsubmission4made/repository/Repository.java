package com.submission.moviecatalogsubmission4made.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.submission.moviecatalogsubmission4made.model.MovieResponse;
import com.submission.moviecatalogsubmission4made.model.TvShowResponse;
import com.submission.moviecatalogsubmission4made.service.ServiceGenerator;
import com.submission.moviecatalogsubmission4made.service.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static Repository repository;
    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    private ServiceInterface service;
    private Repository() {
        service = ServiceGenerator.createService(ServiceInterface.class);
    }






    public MutableLiveData<MovieResponse> getMovies() {
        final MutableLiveData<MovieResponse> movieData = new MutableLiveData<>();
        service.getMovie().enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    movieData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                movieData.setValue(null);
            }
        });

        return movieData;
    }





    public MutableLiveData<TvShowResponse> getTvShows() {
        final MutableLiveData<TvShowResponse> tvShowData = new MutableLiveData<>();
        service.getTvShow().enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    tvShowData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                tvShowData.setValue(null);
            }
        });

        return tvShowData;
    }
}
