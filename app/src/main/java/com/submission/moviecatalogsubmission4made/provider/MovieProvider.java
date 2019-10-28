package com.submission.moviecatalogsubmission4made.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.submission.moviecatalogsubmission4made.db.MovieHelper;

import java.util.Objects;

import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.CONTENT_URI;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.TABLE_MOVIE;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.AUTHORITY;

public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private MovieHelper movieHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE + "#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        movieHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryAll();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long added;

        if (sUriMatcher.match(uri) == MOVIE) {
            added = movieHelper.insert(values);
        } else {
            added = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;

        if (sUriMatcher.match(uri) == MOVIE_ID) {
            updated = movieHelper.update(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);

        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted;

        if (sUriMatcher.match(uri) == MOVIE_ID) {
            deleted = movieHelper.deleteById(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);

        return deleted;
    }
}
