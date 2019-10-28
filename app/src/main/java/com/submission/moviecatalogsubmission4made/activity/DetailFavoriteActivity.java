package com.submission.moviecatalogsubmission4made.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.submission.moviecatalogsubmission4made.BuildConfig;
import com.submission.moviecatalogsubmission4made.R;
import com.submission.moviecatalogsubmission4made.db.MovieHelper;
import com.submission.moviecatalogsubmission4made.db.TvShowHelper;
import com.submission.moviecatalogsubmission4made.model.Movie;
import com.submission.moviecatalogsubmission4made.model.TvShow;

public class DetailFavoriteActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_FAVORITE = "extra_favorite";
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_TVSHOW = "extra_tvshow";
    public static final String EXTRA_POSITION = "extra_position";
    private String type;
    private int position;
    private Movie movie;
    private TvShow tvShow;
    private MovieHelper movieHelper;
    private TvShowHelper tvShowHelper;

    ProgressBar progressBar;
    private ImageView imgPoster, imgBackdrop;
    private TextView tvTitle, tvYear, tvYearTv, tvVoters, tvVotersTv, tvScore, tvScoreTv, tvDescription;
    FloatingActionButton btnFavorite;

    String poster = "";
    String backdrop="";
    String title = "";
    String year = "";
    String voters = "";
    String score = "";
    String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.loading_detail);
        imgPoster = findViewById(R.id.img_poster_detail);
        imgBackdrop = findViewById(R.id.img_backdrop_detail);
        tvTitle = findViewById(R.id.txt_title_detail);
        tvYear = findViewById(R.id.txt_year_detail);
        tvYearTv = findViewById(R.id.txt_year_detail_tv);
        tvVoters = findViewById(R.id.txt_voters_detail);
        tvVotersTv = findViewById(R.id.txt_voters_detail_tv);
        tvScore = findViewById(R.id.txt_score_detail);
        tvScoreTv = findViewById(R.id.txt_score_detail_tv);
        tvDescription = findViewById(R.id.txt_description_detail);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);
        type = getIntent().getStringExtra(EXTRA_TYPE);

        if (savedInstanceState == null) {

            if (type != null) {
                if (type.equals("movie")) {


                    movieHelper = MovieHelper.getInstance(getApplicationContext());
                    movieHelper.open();

                    initMovieData();
                } else {


                    tvShowHelper = TvShowHelper.getInstance(getApplicationContext());
                    tvShowHelper.open();
                    tvShow = getIntent().getParcelableExtra(EXTRA_TVSHOW);

                    initTvShowData();
                }
            }

            if (movie != null || tvShow != null) {
                position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("poster", poster);
        outState.putString("backdrop", backdrop);
        outState.putString("title", title);
        outState.putString("year", year);
        outState.putString("voters", voters);
        outState.putString("score", score);
        outState.putString("description", description);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        poster = savedInstanceState.getString("poster");
        backdrop = savedInstanceState.getString("backdrop");
        title = savedInstanceState.getString("title");
        year = savedInstanceState.getString("year");
        voters = savedInstanceState.getString("voters");
        score = savedInstanceState.getString("score");
        description = savedInstanceState.getString("description");

        showDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (type.equals("tv_show")) {
            tvShowHelper.close();
        }
    }

    private void initMovieData() {
        movie = getIntent().getParcelableExtra(EXTRA_FAVORITE);

        if (movie != null) {
            poster = movie.getPoster();
            backdrop = movie.getBackdrop();
            title = movie.getTitle();
            year = movie.getYear();
            voters = movie.getVoters();
            score = movie.getScore();
            description = movie.getDescription();
            showDetail();
        }
    }

    private void initTvShowData() {
        tvShow = getIntent().getParcelableExtra(EXTRA_FAVORITE);

        if (tvShow != null) {
            poster = tvShow.getPoster();
            backdrop = tvShow.getBackdrop();
            title = tvShow.getTitle();
            year = tvShow.getYear();
            voters = tvShow.getVoters();
            score = tvShow.getScore();
            description = tvShow.getDescription();
            showDetail();
        }
    }

    @SuppressLint("RestrictedApi")
    private void showDetail() {
        Glide.with(this)
                .load(BuildConfig.BASE_URL_IMG + poster)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(new ColorDrawable(Color.LTGRAY))
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(imgPoster);
        Glide.with(this)
                .load(BuildConfig.BASE_URL_IMG + backdrop)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(new ColorDrawable(Color.LTGRAY))
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(imgBackdrop);
        tvTitle.setText(title);
        tvYear.setText(year);
        tvYearTv.setVisibility(View.VISIBLE);
        tvVoters.setText(voters);
        tvVotersTv.setVisibility(View.VISIBLE);
        tvScore.setText(score);
        tvScoreTv.setVisibility(View.VISIBLE);
        tvDescription.setText(description);
        btnFavorite.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        setBtnFavoriteIcon();
    }



    private void setBtnFavoriteIcon() {
        boolean isFavorited;
        int icon;

        if (type.equals("movie")) {
            isFavorited = movieHelper.getMovie(title, year) > 0;
        } else {
            isFavorited = tvShowHelper.getTvShow(title, year) > 0;
        }

        if (isFavorited) {
            icon = R.drawable.ic_favorite_black_24dp;
        } else {
            icon = R.drawable.ic_favorite_border_black_24dp;
        }

        btnFavorite.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), icon)
        );
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_favorite) {

            Intent intent = new Intent();
            boolean isFavorited;

            if (type.equals("movie")) {
                movie.setPoster(poster);
                movie.setBackdrop(backdrop);
                movie.setTitle(title);
                movie.setYear(year);
                movie.setLanguage("en");

                intent.putExtra(EXTRA_MOVIE, movie);
                isFavorited = movieHelper.getMovie(title, year) > 0;
            } else {
                tvShow.setPoster(poster);
                tvShow.setBackdrop(backdrop);
                tvShow.setTitle(title);
                tvShow.setYear(year);
                tvShow.setLanguage("en");

                intent.putExtra(EXTRA_TVSHOW, tvShow);
                isFavorited = tvShowHelper.getTvShow(title, year) > 0;
            }

            intent.putExtra(EXTRA_POSITION, position);
            String message;

            if (!isFavorited) {
                long result;

                if (type.equals("movie")) {
                    result = movieHelper.insertMovie(movie);
                } else {
                    result = tvShowHelper.insertTvShow(tvShow);
                }

                if (result > 0) {
                    if (type.equals("movie")) {
                        movie.setId((int) result);
                    } else {
                        tvShow.setId((int) result);
                    }

                    setBtnFavoriteIcon();
                    message = "Add "+title+" to favorite";
                } else {
                    message = "Error add favorite";
                }
            } else {
                long delete;

                if (type.equals("movie")) {
                    delete = movieHelper.deleteMovie(title, year);
                } else {
                    delete = tvShowHelper.deleteTvShow(title, year);
                }

                if (delete > 0) {
                    Intent delIntent = new Intent();
                    delIntent.putExtra(EXTRA_POSITION, position);
                    setBtnFavoriteIcon();
                    message = "Remove "+title+" from favorite";
                } else {
                    message = "Error remove favorite";
                }
            }

            Toast.makeText(DetailFavoriteActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
