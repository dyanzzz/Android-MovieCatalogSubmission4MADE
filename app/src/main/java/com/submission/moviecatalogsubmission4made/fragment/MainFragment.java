package com.submission.moviecatalogsubmission4made.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.submission.moviecatalogsubmission4made.R;
import com.submission.moviecatalogsubmission4made.activity.DetailActivity;
import com.submission.moviecatalogsubmission4made.adapter.MovieAdapter;
import com.submission.moviecatalogsubmission4made.adapter.TvShowAdapter;
import com.submission.moviecatalogsubmission4made.model.Movie;
import com.submission.moviecatalogsubmission4made.model.MovieResponse;
import com.submission.moviecatalogsubmission4made.model.TvShow;
import com.submission.moviecatalogsubmission4made.model.TvShowResponse;
import com.submission.moviecatalogsubmission4made.model.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView rvMovies;
    private ProgressBar progressBar;

    private List<Movie> listMovie = new ArrayList<>();
    private List<TvShow> listTvShow = new ArrayList<>();
    public static final String EXTRA_OBJECT = "EXTRA_OBJECT";
    private MainViewModel mainViewModel;

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        rvMovies = view.findViewById(R.id.recyclerview);
        rvMovies.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.loading);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.init();

        Bundle args = getArguments();
        if (args != null) initArray(args.getString(EXTRA_OBJECT));

        showLoading();
        return view;
    }

    private void initArray(String tab) {
        if (listMovie.isEmpty() && listTvShow.isEmpty()) {
            if (tab.equals("movies")) {
                mainViewModel.getMovies().observe(this, getMovie);
            } else {
                mainViewModel.getTvShows().observe(this, getTvShow);
            }
        }
    }





    private Observer<MovieResponse> getMovie = new Observer<MovieResponse>() {
        @Override
        public void onChanged(MovieResponse movieReponse) {
            if (movieReponse != null) {
                List<Movie> movies = movieReponse.getMovies();
                listMovie.addAll(movies);
                showMovies();
            } else {
                Toast.makeText(getContext(), "Error get movies", Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        }
    };

    private void showMovies() {
        MovieAdapter movieAdapter = new MovieAdapter(listMovie);
        rvMovies.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data) {
                openDetail("movie", data.getId());
            }
        });
        hideLoading();
    }





    private Observer<TvShowResponse> getTvShow = new Observer<TvShowResponse>() {
        @Override
        public void onChanged(TvShowResponse tvShowResponse) {
            if (tvShowResponse != null) {
                List<TvShow> tvShows = tvShowResponse.getTvShow();
                listTvShow.addAll(tvShows);
                showTvShows();
            } else {
                Toast.makeText(getContext(), "Error get tv show", Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        }
    };

    private void showTvShows() {
        TvShowAdapter tvShowAdapter = new TvShowAdapter(listTvShow);
        rvMovies.setAdapter(tvShowAdapter);
        tvShowAdapter.notifyDataSetChanged();

        tvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow data) {
                openDetail("tv_show", data.getId());
            }
        });
        hideLoading();
    }

    private void openDetail(String type, int id) {

        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_ID, id);
        intent.putExtra(DetailActivity.EXTRA_TYPE, type);
        startActivity(intent);

    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
