package com.submission.moviecatalogsubmission4made.service;

import com.submission.moviecatalogsubmission4made.model.Movie;
import com.submission.moviecatalogsubmission4made.model.MovieResponse;
import com.submission.moviecatalogsubmission4made.model.TvShow;
import com.submission.moviecatalogsubmission4made.model.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.submission.moviecatalogsubmission4made.service.ServiceGenerator.API_KEY;

public interface ServiceInterface {
    @GET("discover/movie?api_key=" + API_KEY + "&language=en-US")
    Call<MovieResponse> getMovie();

    @GET("movie/{id}?api_key=" + API_KEY + "&language=en-US")
    Call<Movie> getMovieDetail(@Path("id") int id);

    @GET("discover/tv?api_key=" + API_KEY + "&language=en-US")
    Call<TvShowResponse> getTvShow();

    @GET("tv/{id}?api_key=" + API_KEY + "&language=en-US")
    Call<TvShow> getTvShowDetail(@Path("id") int id);

}
