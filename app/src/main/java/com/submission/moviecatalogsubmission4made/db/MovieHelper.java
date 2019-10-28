package com.submission.moviecatalogsubmission4made.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.submission.moviecatalogsubmission4made.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.BACKDROP;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.DESCRIPTION;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.LANGUAGE;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.POSTER;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.RELEASE_DATE;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.SCORE;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.TITLE;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.DbColumns.VOTERS;
import static com.submission.moviecatalogsubmission4made.db.DatabaseContract.TABLE_MOVIE;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DatabaseHelper databaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();

        Movie movie;

        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                movie.setBackdrop(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setYear(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movie.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));
                movie.setVoters(cursor.getString(cursor.getColumnIndexOrThrow(VOTERS)));
                movie.setScore(cursor.getString(cursor.getColumnIndexOrThrow(SCORE)));
                movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));

                movies.add(movie);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return movies;
    }

    public int getMovie(String title, String year) {
        String query = "SELECT * FROM " + TABLE_MOVIE +
                " WHERE " + TITLE + " = '" + title.replace("'", "''") + "'" +
                " AND " + RELEASE_DATE + " = '" + year + "'";
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(MovieHelper.class.getSimpleName(), e.toString());
        }

        if (cursor != null)
            cursor.close();

        return cursor.getCount();
    }

    public int getMovieId(String title, String year) {
        String query = "SELECT * FROM " + TABLE_MOVIE +
                " WHERE " + TITLE + " = '" + title.replace("'", "''") + "'" +
                " AND " + RELEASE_DATE + " = '" + year + "'";
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(MovieHelper.class.getSimpleName(), e.toString());
        }

        if (cursor != null)
            cursor.close();

        return cursor != null ? cursor.getColumnIndexOrThrow(_ID) : 0;
    }

    public Cursor queryAll() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC");
    }

    public Cursor queryById(String id) {
        return database.query(DATABASE_TABLE, null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

    public long insertMovie(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(POSTER, movie.getPoster());
        args.put(BACKDROP, movie.getBackdrop());
        args.put(TITLE, movie.getTitle());
        args.put(RELEASE_DATE, movie.getYear());
        args.put(LANGUAGE, movie.getLanguage());
        args.put(VOTERS, movie.getVoters());
        args.put(SCORE, movie.getScore());
        args.put(DESCRIPTION, movie.getDescription());

        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovie(String title, String year) {
        return database.delete(
                TABLE_MOVIE,
                TITLE + " = '" + title.replace("'", "''") + "' AND " + RELEASE_DATE + " = '" + year + "'",
                null
        );
    }
}
