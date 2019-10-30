package com.submission.moviecatalogsubmission4made.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.submission.moviecatalogsubmission4made.R;
import com.submission.moviecatalogsubmission4made.activity.DetailFavoriteActivity;
import com.submission.moviecatalogsubmission4made.adapter.MovieAdapter;
import com.submission.moviecatalogsubmission4made.adapter.TvShowAdapter;
import com.submission.moviecatalogsubmission4made.db.TvShowHelper;
import com.submission.moviecatalogsubmission4made.helper.MappingHelper;
import com.submission.moviecatalogsubmission4made.model.Movie;
import com.submission.moviecatalogsubmission4made.model.TvShow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.CONTENT_URI;
import static com.submission.moviecatalogsubmission4made.fragment.MainFragment.EXTRA_OBJECT;

public class FavoriteListFragment extends Fragment implements LoadFavoritesCallback {

    private RecyclerView rvFavorite;
    private ProgressBar progressBar;
    private TvShowHelper tvShowHelper;
    private MovieAdapter movieAdapter;
    private TvShowAdapter tvShowAdapter;
    private TextView textViewNotFound;

    private final String TYPE_MOVIES = "movies";
    private String type;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        progressBar = view.findViewById(R.id.progress_list);
        rvFavorite = view.findViewById(R.id.rv_list);
        textViewNotFound = view.findViewById(R.id.textViewNotFound);

        rvFavorite.setHasFixedSize(true);

        Bundle args = getArguments();

        if (args != null) {
            type = args.getString(EXTRA_OBJECT);
            openDatabase();

            if (type != null) {
                if (type.equals(TYPE_MOVIES)) {
                    movieAdapter = new MovieAdapter();
                    rvFavorite.setAdapter(movieAdapter);

                    movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(Movie data) {
                            openMovieDetail(data);
                        }
                    });
                } else {
                    tvShowAdapter = new TvShowAdapter();
                    rvFavorite.setAdapter(tvShowAdapter);

                    tvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(TvShow data) {
                            openTvShowDetail(data);
                        }
                    });
                }
            }
        }

        if (type != null) {
            if (type.equals(TYPE_MOVIES)) {
                new LoadMoviesAsync(getContext(), this).execute();
            } else {
                new LoadTvShowsAsync(tvShowHelper, this).execute();
            }
        }

        return view;
    }

    private void openMovieDetail(Movie data) {
        Movie movie = new Movie();
        movie.setId(data.getId());
        movie.setPoster(data.getPoster());
        movie.setBackdrop(data.getBackdrop());
        movie.setTitle(data.getTitle());
        movie.setYear(data.getYear());
        movie.setVoters(data.getVoters());
        movie.setScore(data.getScore());
        movie.setDescription(data.getDescription());

        Intent intent = new Intent(getContext(), DetailFavoriteActivity.class);
        intent.putExtra(DetailFavoriteActivity.EXTRA_FAVORITE, movie);
        intent.putExtra(DetailFavoriteActivity.EXTRA_TYPE, "movie");
        startActivity(intent);
    }

    private void openTvShowDetail(TvShow data) {
        TvShow tvShow = new TvShow();
        tvShow.setId(data.getId());
        tvShow.setPoster(data.getPoster());
        tvShow.setBackdrop(data.getBackdrop());
        tvShow.setTitle(data.getTitle());
        tvShow.setYear(data.getYear());
        tvShow.setVoters(data.getVoters());
        tvShow.setScore(data.getScore());
        tvShow.setDescription(data.getDescription());

        Intent intent = new Intent(getContext(), DetailFavoriteActivity.class);
        intent.putExtra(DetailFavoriteActivity.EXTRA_FAVORITE, tvShow);
        intent.putExtra(DetailFavoriteActivity.EXTRA_TYPE, "tv_show");
        startActivity(intent);
    }

    private void openDatabase() {
        if (type.equals(TYPE_MOVIES)) {
            HandlerThread handlerThread = new HandlerThread("DataObserver");
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper());

            DataObserver dataObserver = new DataObserver(handler, getContext());
            Objects.requireNonNull(getContext()).getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);
        } else {
            tvShowHelper = TvShowHelper.getInstance(getContext());
            tvShowHelper.open();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!type.equals(TYPE_MOVIES)) {
            tvShowHelper.close();
        }
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
        textViewNotFound.setVisibility(View.GONE);
    }

    @Override
    public void postExecuteMovie(List<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);

        if (movies.size() > 0) {
            movieAdapter.setData(movies);
            textViewNotFound.setVisibility(View.GONE);
        } else {
            movieAdapter.setData(new ArrayList<Movie>());
            textViewNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void postExecuteTvShow(List<TvShow> tvShows) {
        progressBar.setVisibility(View.INVISIBLE);

        if (tvShows.size() > 0) {
            tvShowAdapter.setData(tvShows);
            textViewNotFound.setVisibility(View.GONE);
        } else {
            tvShowAdapter.setData(new ArrayList<TvShow>());
            textViewNotFound.setVisibility(View.VISIBLE);
        }
    }






    private static class LoadMoviesAsync extends AsyncTask<Void, Void, List<Movie>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoritesCallback> weakCallback;

        private LoadMoviesAsync(Context context, LoadFavoritesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToList(Objects.requireNonNull(dataCursor));
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecuteMovie(movies);
        }
    }







    private static class LoadTvShowsAsync extends AsyncTask<Void, Void, List<TvShow>> {
        private final WeakReference<TvShowHelper> weakTvShowHelper;
        private final WeakReference<LoadFavoritesCallback> weakCallback;

        private LoadTvShowsAsync(TvShowHelper tvShowHelper, LoadFavoritesCallback callback) {
            weakTvShowHelper = new WeakReference<>(tvShowHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected List<TvShow> doInBackground(Void... voids) {
            return weakTvShowHelper.get().getAllTvShow();
        }

        @Override
        protected void onPostExecute(List<TvShow> tvShows) {
            super.onPostExecute(tvShows);
            weakCallback.get().postExecuteTvShow(tvShows);
        }
    }






    public static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMoviesAsync(context, (LoadFavoritesCallback) context).execute();
        }
    }
}
