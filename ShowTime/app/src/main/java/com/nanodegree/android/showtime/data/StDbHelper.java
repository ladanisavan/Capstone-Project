package com.nanodegree.android.showtime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for shows data.
 */
public class StDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "showtime.db";

    public StDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_SHOW_TABLE = "CREATE TABLE " +
                StContract.ShowEntry.TABLE_NAME + " (" +
                StContract.ShowEntry._ID + " INTEGER PRIMARY KEY," +
                StContract.ShowEntry.COLUMN_TITLE + " TEXT, " +
                StContract.ShowEntry.COLUMN_OVERVIEW + " TEXT, " +
                StContract.ShowEntry.COLUMN_POSTER_PATH + " TEXT, " +
                StContract.ShowEntry.COLUMN_BANNER_PATH + " TEXT, " +
                StContract.ShowEntry.COLUMN_THUMB_PATH + " TEXT, " +
                StContract.ShowEntry.COLUMN_STATUS + " TEXT, " +
                StContract.ShowEntry.COLUMN_YEAR + " INTEGER, " +
                StContract.ShowEntry.COLUMN_FIRST_AIRED + " INTEGER, " +
                StContract.ShowEntry.COLUMN_AIR_DAY + " TEXT, " +
                StContract.ShowEntry.COLUMN_RUNTIME + " INTEGER, " +
                StContract.ShowEntry.COLUMN_NETWORK + " TEXT, " +
                StContract.ShowEntry.COLUMN_COUNTRY + " TEXT, " +
                StContract.ShowEntry.COLUMN_HOMEPAGE + " TEXT, " +
                StContract.ShowEntry.COLUMN_RATING + " REAL, " +
                StContract.ShowEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                StContract.ShowEntry.COLUMN_LANGUAGE + " TEXT, " +
                StContract.ShowEntry.COLUMN_AIRED_EPISODES + " INTEGER, " +
                StContract.ShowEntry.COLUMN_WATCHING + " INTEGER DEFAULT 0, " +
                StContract.ShowEntry.COLUMN_WATCHED + " INTEGER DEFAULT 0, " +
                StContract.ShowEntry.COLUMN_WATCHLIST + " INTEGER DEFAULT 0, " +
                StContract.ShowEntry.COLUMN_WTA_UPDATE_DATE + " INTEGER, " +
                StContract.ShowEntry.COLUMN_LAST_SEARCH_RESULT + " INTEGER DEFAULT 0, " +
                StContract.ShowEntry.COLUMN_SEARCH_SCORE + " REAL, " +
                StContract.ShowEntry.COLUMN_POPULARITY + " INTEGER DEFAULT 0 " +
                ");";

        final String SQL_CREATE_SHOW_GENRE_TABLE = "CREATE TABLE " +
                StContract.ShowEntry.GENRE_RELATION_TABLE_NAME + " (" +
                StContract.ShowEntry.COLUMN_SHOW_ID + " INTEGER NOT NULL, " +
                StContract.ShowEntry.COLUMN_GENRE_ID + " INTEGER NOT NULL, " +
                " PRIMARY KEY (" + StContract.ShowEntry.COLUMN_SHOW_ID + ", " +
                StContract.ShowEntry.COLUMN_GENRE_ID + "), " +
                " FOREIGN KEY (" + StContract.ShowEntry.COLUMN_SHOW_ID + ") REFERENCES " +
                StContract.ShowEntry.TABLE_NAME + " (" + StContract.ShowEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE, " +
                " FOREIGN KEY (" + StContract.ShowEntry.COLUMN_GENRE_ID + ") REFERENCES " +
                StContract.GenreEntry.TABLE_NAME + " (" + StContract.GenreEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_SHOW_PEOPLE_TABLE = "CREATE TABLE " +
                StContract.ShowEntry.PERSON_RELATION_TABLE_NAME + " (" +
                StContract.ShowEntry.COLUMN_SHOW_ID + " INTEGER NOT NULL, " +
                StContract.ShowEntry.COLUMN_PERSON_ID + " INTEGER NOT NULL, " +
                StContract.ShowEntry.COLUMN_CHARACTER + " TEXT, " +
                " PRIMARY KEY (" + StContract.ShowEntry.COLUMN_SHOW_ID + ", " +
                StContract.ShowEntry.COLUMN_PERSON_ID + ", " +
                StContract.ShowEntry.COLUMN_CHARACTER + "), " +
                " FOREIGN KEY (" + StContract.ShowEntry.COLUMN_SHOW_ID + ") REFERENCES " +
                StContract.ShowEntry.TABLE_NAME + " (" + StContract.ShowEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE, " +
                " FOREIGN KEY (" + StContract.ShowEntry.COLUMN_PERSON_ID + ") REFERENCES " +
                StContract.PersonEntry.TABLE_NAME + " (" + StContract.PersonEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_SEASON_TABLE = "CREATE TABLE " +
                StContract.SeasonEntry.TABLE_NAME + " (" +
                StContract.SeasonEntry._ID + " INTEGER PRIMARY KEY," +
                StContract.SeasonEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                StContract.SeasonEntry.COLUMN_EPISODE_COUNT + " INTEGER, " +
                StContract.SeasonEntry.COLUMN_AIRED_EPISODES + " INTEGER, " +
                StContract.SeasonEntry.COLUMN_FIRST_AIRED + " INTEGER, " +
                StContract.SeasonEntry.COLUMN_SHOW_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + StContract.SeasonEntry.COLUMN_SHOW_ID + ") REFERENCES " +
                StContract.ShowEntry.TABLE_NAME + " (" + StContract.ShowEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_EPISODE_TABLE = "CREATE TABLE " +
                StContract.EpisodeEntry.TABLE_NAME + " (" +
                StContract.EpisodeEntry._ID + " INTEGER PRIMARY KEY," +
                StContract.EpisodeEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                StContract.EpisodeEntry.COLUMN_TITLE + " TEXT, " +
                StContract.EpisodeEntry.COLUMN_OVERVIEW + " TEXT, " +
                StContract.EpisodeEntry.COLUMN_SCREENSHOT_PATH + " TEXT, " +
                StContract.EpisodeEntry.COLUMN_FIRST_AIRED + " INTEGER, " +
                StContract.EpisodeEntry.COLUMN_RATING + " REAL, " +
                StContract.EpisodeEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                StContract.EpisodeEntry.COLUMN_SEASON_NUMBER + " INTEGER NOT NULL, " +
                StContract.EpisodeEntry.COLUMN_SEASON_ID + " INTEGER NOT NULL, " +
                StContract.EpisodeEntry.COLUMN_WATCHED + " INTEGER DEFAULT 0, " +
                StContract.EpisodeEntry.COLUMN_WATCHLIST + " INTEGER DEFAULT 0, " +
                " FOREIGN KEY (" + StContract.EpisodeEntry.COLUMN_SEASON_ID + ") REFERENCES " +
                StContract.SeasonEntry.TABLE_NAME + " (" + StContract.SeasonEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_COMMENT_TABLE = "CREATE TABLE " +
                StContract.CommentEntry.TABLE_NAME + " (" +
                StContract.CommentEntry._ID + " INTEGER PRIMARY KEY," +
                StContract.CommentEntry.COLUMN_CREATED_AT + " INTEGER, " +
                StContract.CommentEntry.COLUMN_CONTENT + " TEXT, " +
                StContract.CommentEntry.COLUMN_SPOILER + " INTEGER DEFAULT 0 NOT NULL, " +
                StContract.CommentEntry.COLUMN_REVIEW + " INTEGER DEFAULT 0 NOT NULL, " +
                StContract.CommentEntry.COLUMN_LIKES + " INTEGER, " +
                StContract.CommentEntry.COLUMN_USER + " TEXT, " +
                StContract.CommentEntry.COLUMN_SHOW_ID+ " INTEGER, " +
                StContract.CommentEntry.COLUMN_EPISODE_ID + " INTEGER, " +
                " FOREIGN KEY (" + StContract.CommentEntry.COLUMN_SHOW_ID + ") REFERENCES " +
                StContract.ShowEntry.TABLE_NAME + " (" + StContract.ShowEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE, " +
                " FOREIGN KEY (" + StContract.CommentEntry.COLUMN_EPISODE_ID + ") REFERENCES " +
                StContract.EpisodeEntry.TABLE_NAME + " (" + StContract.EpisodeEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " +
                StContract.GenreEntry.TABLE_NAME + " (" +
                StContract.GenreEntry._ID + " TEXT PRIMARY KEY," +
                StContract.GenreEntry.COLUMN_NAME + " TEXT " +
                ");";

        final String SQL_CREATE_PERSON_TABLE = "CREATE TABLE " +
                StContract.PersonEntry.TABLE_NAME + " (" +
                StContract.PersonEntry._ID + " INTEGER PRIMARY KEY," +
                StContract.PersonEntry.COLUMN_NAME + " TEXT, " +
                StContract.PersonEntry.COLUMN_HEADSHOT_PATH + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_SHOW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SHOW_GENRE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SHOW_PEOPLE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SEASON_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EPISODE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_COMMENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GENRE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PERSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StContract.ShowEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StContract.ShowEntry.GENRE_RELATION_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StContract.ShowEntry.PERSON_RELATION_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StContract.SeasonEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StContract.EpisodeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StContract.CommentEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StContract.GenreEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StContract.PersonEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
