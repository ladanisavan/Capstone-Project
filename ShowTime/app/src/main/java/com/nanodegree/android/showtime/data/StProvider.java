package com.nanodegree.android.showtime.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.nanodegree.android.showtime.util.Utility;

/**
 * A Content Provider for ShowTime show data
 */
public class StProvider extends ContentProvider {

    //"show._id = ?" selection String
    public static final String sShowSelection = StContract.ShowEntry.TABLE_NAME + "." +
            StContract.ShowEntry._ID + " = ? ";
    //"show.watching = ?" selection String
    public static final String sWatchingShowSelection = StContract.ShowEntry.TABLE_NAME + "." +
            StContract.ShowEntry.COLUMN_WATCHING + " = ? ";
    //"show.watched = ?" selection String
    public static final String sWatchedShowSelection = StContract.ShowEntry.TABLE_NAME + "." +
            StContract.ShowEntry.COLUMN_WATCHED + " = ? ";
    //"show.watchlist = ?" selection String
    public static final String sWatchlistShowSelection = StContract.ShowEntry.TABLE_NAME + "." +
            StContract.ShowEntry.COLUMN_WATCHLIST + " = ? ";
    //"show.popularity > ?" selection String
    public static final String sPopularityShowSelection = StContract.ShowEntry.TABLE_NAME + "." +
            StContract.ShowEntry.COLUMN_POPULARITY + " > ? ";
    //"show.last_search_result = ?" selection String
    public static final String sLastSearchedShowSelection = StContract.ShowEntry.TABLE_NAME + "." +
            StContract.ShowEntry.COLUMN_LAST_SEARCH_RESULT + " = ? ";
    //"show.update_date < ?" selection String (always show.watching=0 && show.watched=0 && show.watchlist=0
    public static final String sShowsByUpdateDateSelection = StContract.ShowEntry.TABLE_NAME + "." +
            StContract.ShowEntry.COLUMN_WTA_UPDATE_DATE + " < ? AND " +
            StContract.ShowEntry.TABLE_NAME + "." + StContract.ShowEntry.COLUMN_WATCHING + " = 0 AND " +
            StContract.ShowEntry.TABLE_NAME + "." + StContract.ShowEntry.COLUMN_WATCHED + " = 0 AND " +
            StContract.ShowEntry.TABLE_NAME + "." + StContract.ShowEntry.COLUMN_WATCHLIST + " = 0 ";
    //"season._id = ?" selection String
    public static final String sSeasonSelection = StContract.SeasonEntry.TABLE_NAME + "." +
            StContract.SeasonEntry._ID + " = ? ";
    //"episode._id = ?" selection String
    public static final String sEpisodeSelection = StContract.EpisodeEntry.TABLE_NAME + "." +
            StContract.EpisodeEntry._ID + " = ? ";
    //"comment._id = ?" selection String
    public static final String sCommentSelection = StContract.CommentEntry.TABLE_NAME + "." +
            StContract.CommentEntry._ID + " = ? ";
    //"genre._id = ?" selection String
    public static final String sGenreSelection = StContract.GenreEntry.TABLE_NAME + "." +
            StContract.GenreEntry._ID + " = ? ";
    //"person._id = ?" selection String
    public static final String sPersonSelection = StContract.PersonEntry.TABLE_NAME + "." +
            StContract.PersonEntry._ID + " = ? ";
    //"showgenre.show_id = ? and showgenre.genre_id = ?" selection String
    public static final String sShowGenreSelection = StContract.ShowEntry.GENRE_RELATION_TABLE_NAME +
            "." + StContract.ShowEntry.COLUMN_SHOW_ID + " = ? AND " + StContract.ShowEntry.GENRE_RELATION_TABLE_NAME +
            "." + StContract.ShowEntry.COLUMN_GENRE_ID + " = ?";
    //"showperson.show_id = ? and showgenre.person_id = ? and showgenre.character = ?" selection String
    public static final String sShowPersonSelection = StContract.ShowEntry.PERSON_RELATION_TABLE_NAME +
            "." + StContract.ShowEntry.COLUMN_SHOW_ID + " = ? AND " + StContract.ShowEntry.PERSON_RELATION_TABLE_NAME +
            "." + StContract.ShowEntry.COLUMN_PERSON_ID + " = ? AND " + StContract.ShowEntry.PERSON_RELATION_TABLE_NAME +
            "." + StContract.ShowEntry.COLUMN_CHARACTER + " = ?";
    //"season.show_id = ?" selection String
    public static final String sSeasonByShowIdSelection = StContract.SeasonEntry.TABLE_NAME +
            "." + StContract.SeasonEntry.COLUMN_SHOW_ID + " = ? ";
    //"episode.season_id = ?" selection String
    public static final String sEpisodeBySeasonIdSelection = StContract.EpisodeEntry.TABLE_NAME +
            "." + StContract.EpisodeEntry.COLUMN_SEASON_ID + " = ? ";
    //"comment.episode_id = ?" selection String
    public static final String sCommentByEpisodeIdSelection = StContract.CommentEntry.TABLE_NAME +
            "." + StContract.CommentEntry.COLUMN_EPISODE_ID + " = ? ";
    //"comment.show_id = ?" selection String
    public static final String sCommentByShowIdSelection = StContract.CommentEntry.TABLE_NAME +
            "." + StContract.CommentEntry.COLUMN_SHOW_ID + " = ? ";
    //"showgenre.show_id = ?" selection String
    public static final String sGenreByShowIdSelection = StContract.ShowEntry.GENRE_RELATION_TABLE_NAME +
            "." + StContract.ShowEntry.COLUMN_SHOW_ID + " = ? ";
    //"showperson.show_id = ?" selection String
    public static final String sPersonByShowIdSelection = StContract.ShowEntry.PERSON_RELATION_TABLE_NAME +
            "." + StContract.ShowEntry.COLUMN_SHOW_ID + " = ? ";

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sShowQueryBuilder;
    private static final SQLiteQueryBuilder sSeasonQueryBuilder;
    private static final SQLiteQueryBuilder sEpisodeQueryBuilder;
    private static final SQLiteQueryBuilder sCommentQueryBuilder;
    private static final SQLiteQueryBuilder sGenreQueryBuilder;
    private static final SQLiteQueryBuilder sShowGenreQueryBuilder;
    private static final SQLiteQueryBuilder sPersonQueryBuilder;
    private static final SQLiteQueryBuilder sShowPersonQueryBuilder;
    private static final int SHOWS = 100;
    private static final int SHOW_WITH_ID = 101;
    private static final int SEASONS = 200;
    private static final int SEASON_WITH_ID = 201;
    private static final int SHOW_SEASONS_WITH_ID = 202;
    private static final int EPISODES = 300;
    private static final int EPISODE_WITH_ID = 301;
    private static final int SEASON_EPISODES_WITH_ID = 302;
    private static final int COMMENTS = 400;
    private static final int COMMENT_WITH_ID = 401;
    private static final int SHOW_COMMENTS_WITH_ID = 402;
    private static final int EPISODE_COMMENTS_WITH_ID = 403;
    private static final int GENRES = 500;
    private static final int GENRE_WITH_ID = 501;
    private static final int SHOW_GENRES_WITH_ID = 502;
    private static final int PEOPLE = 600;
    private static final int PERSON_WITH_ID = 601;
    private static final int SHOW_PEOPLE_WITH_ID = 602;
    private static final int SHOW_GENRES = 700;
    private static final int SHOW_GENRE_RELATION_WITH_ID = 701;
    private static final int SHOW_PEOPLE = 800;
    private static final int SHOW_PERSON_RELATION_WITH_ID = 801;

    private StDbHelper mDbHelper;

    static{
        sShowQueryBuilder = new SQLiteQueryBuilder();
        sShowQueryBuilder.setTables(StContract.ShowEntry.TABLE_NAME);
        sSeasonQueryBuilder = new SQLiteQueryBuilder();
        sSeasonQueryBuilder.setTables(StContract.SeasonEntry.TABLE_NAME);
        sEpisodeQueryBuilder = new SQLiteQueryBuilder();
        sEpisodeQueryBuilder.setTables(StContract.EpisodeEntry.TABLE_NAME);
        sCommentQueryBuilder = new SQLiteQueryBuilder();
        sCommentQueryBuilder.setTables(StContract.CommentEntry.TABLE_NAME);
        sGenreQueryBuilder = new SQLiteQueryBuilder();
        sGenreQueryBuilder.setTables(StContract.GenreEntry.TABLE_NAME);
        sShowGenreQueryBuilder = new SQLiteQueryBuilder();
        sShowGenreQueryBuilder.setTables(
                StContract.ShowEntry.GENRE_RELATION_TABLE_NAME + " LEFT JOIN " +
                        StContract.GenreEntry.TABLE_NAME + " ON " +
                        StContract.ShowEntry.GENRE_RELATION_TABLE_NAME + "." +
                        StContract.ShowEntry.COLUMN_GENRE_ID + " = " +
                        StContract.GenreEntry.TABLE_NAME + "." +
                        StContract.GenreEntry._ID);
        sPersonQueryBuilder = new SQLiteQueryBuilder();
        sPersonQueryBuilder.setTables(StContract.PersonEntry.TABLE_NAME);
        sShowPersonQueryBuilder = new SQLiteQueryBuilder();
        sShowPersonQueryBuilder.setTables(StContract.PersonEntry.TABLE_NAME + " INNER JOIN " +
                StContract.ShowEntry.PERSON_RELATION_TABLE_NAME + " ON " +
                StContract.PersonEntry.TABLE_NAME + "." +
                StContract.PersonEntry._ID + " = " +
                StContract.ShowEntry.PERSON_RELATION_TABLE_NAME + "." +
                StContract.ShowEntry.COLUMN_PERSON_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new StDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOWS:
                return StContract.ShowEntry.CONTENT_TYPE;
            case SHOW_WITH_ID:
                return StContract.ShowEntry.CONTENT_ITEM_TYPE;
            case SEASONS:
                return StContract.SeasonEntry.CONTENT_TYPE;
            case SHOW_SEASONS_WITH_ID:
                return StContract.SeasonEntry.CONTENT_TYPE;
            case SEASON_WITH_ID:
                return StContract.SeasonEntry.CONTENT_ITEM_TYPE;
            case EPISODES:
                return StContract.EpisodeEntry.CONTENT_TYPE;
            case EPISODE_WITH_ID:
                return StContract.EpisodeEntry.CONTENT_ITEM_TYPE;
            case SEASON_EPISODES_WITH_ID:
                return StContract.EpisodeEntry.CONTENT_TYPE;
            case COMMENTS:
                return StContract.CommentEntry.CONTENT_TYPE;
            case COMMENT_WITH_ID:
                return StContract.CommentEntry.CONTENT_ITEM_TYPE;
            case SHOW_COMMENTS_WITH_ID:
                return StContract.CommentEntry.CONTENT_TYPE;
            case EPISODE_COMMENTS_WITH_ID:
                return StContract.CommentEntry.CONTENT_TYPE;
            case GENRES:
                return StContract.GenreEntry.CONTENT_TYPE;
            case SHOW_GENRES_WITH_ID:
                return StContract.GenreEntry.CONTENT_TYPE;
            case GENRE_WITH_ID:
                return StContract.GenreEntry.CONTENT_ITEM_TYPE;
            case PEOPLE:
                return StContract.PersonEntry.CONTENT_TYPE;
            case SHOW_PEOPLE_WITH_ID:
                return StContract.PersonEntry.CONTENT_TYPE;
            case PERSON_WITH_ID:
                return StContract.PersonEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "shows/*"
            case SHOW_WITH_ID:
            {
                retCursor = getShowById(uri, projection, sortOrder);
                break;
            }
            // "shows"
            case SHOWS: {
                retCursor = getShows(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "seasons/*"
            case SEASON_WITH_ID:
            {
                retCursor = getSeasonById(uri, projection, sortOrder);
                break;
            }
            // "shows/seasons/*"
            case SHOW_SEASONS_WITH_ID:
            {
                retCursor = getSeasonsByShowId(uri, projection, sortOrder);
                break;
            }
            // "seasons"
            case SEASONS: {
                retCursor = getSeasons(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "seasons/episodes/*"
            case SEASON_EPISODES_WITH_ID:
            {
                retCursor = getEpisodesBySeasonId(uri, projection, sortOrder);
                break;
            }
            // "episodes/*"
            case EPISODE_WITH_ID:
            {
                retCursor = getEpisodeById(uri, projection, sortOrder);
                break;
            }
            // "episodes"
            case EPISODES: {
                retCursor = getEpisodes(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "episodes/comments/*"
            case EPISODE_COMMENTS_WITH_ID:
            {
                retCursor = getCommentsByEpisodeId(uri, projection, sortOrder);
                break;
            }
            // "shows/comments/*"
            case SHOW_COMMENTS_WITH_ID:
            {
                retCursor = getCommentsByShowId(uri, projection, sortOrder);
                break;
            }
            // "comments/*"
            case COMMENT_WITH_ID:
            {
                retCursor = getCommentById(uri, projection, sortOrder);
                break;
            }
            // "comments"
            case COMMENTS: {
                retCursor = getComments(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "genres/*"
            case GENRE_WITH_ID:
            {
                retCursor = getGenreById(uri, projection, sortOrder);
                break;
            }
            // "showgenres/*"
            case SHOW_GENRE_RELATION_WITH_ID:
            {
                retCursor = getGenresByShowId(uri, projection, sortOrder);
                break;
            }
            // "genres"
            case GENRES: {
                retCursor = getGenres(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "people/*"
            case PERSON_WITH_ID:
            {
                retCursor = getPersonById(uri, projection, sortOrder);
                break;
            }
            // "showpeople/*"
            case SHOW_PERSON_RELATION_WITH_ID:
            {
                retCursor = getPeopleByShowId(uri, projection, sortOrder);
                break;
            }
            // "people"
            case PEOPLE: {
                retCursor = getPeople(projection, selection, selectionArgs, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case SHOWS: {
                long _id = db.insert(StContract.ShowEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = StContract.ShowEntry.buildShowUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SEASONS: {
                long _id = db.insert(StContract.SeasonEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = StContract.SeasonEntry.buildSeasonUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case EPISODES: {
                long _id = db.insert(StContract.EpisodeEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = StContract.EpisodeEntry.buildEpisodeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case COMMENTS: {
                long _id = db.insert(StContract.CommentEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = StContract.CommentEntry.buildCommentUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case GENRES: {
                long _id = db.insert(StContract.GenreEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = StContract.GenreEntry.buildGenreUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PEOPLE: {
                long _id = db.insert(StContract.PersonEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = StContract.PersonEntry.buildPersonUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SHOW_GENRES: {
                long _id = db.insert(StContract.ShowEntry.GENRE_RELATION_TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = StContract.ShowEntry.buildShowGenreUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SHOW_PEOPLE: {
                long _id = db.insert(StContract.ShowEntry.PERSON_RELATION_TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = StContract.ShowEntry.buildShowPersonUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case SHOWS:
                rowsDeleted = db.delete(StContract.ShowEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case SEASONS:
                rowsDeleted = db.delete(StContract.SeasonEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case EPISODES:
                rowsDeleted = db.delete(StContract.EpisodeEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case COMMENTS:
                rowsDeleted = db.delete(StContract.CommentEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case GENRES:
                rowsDeleted = db.delete(StContract.GenreEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case PEOPLE:
                rowsDeleted = db.delete(StContract.PersonEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case SHOW_GENRES:
                rowsDeleted = db.delete(StContract.ShowEntry.GENRE_RELATION_TABLE_NAME,
                        selection, selectionArgs);
                break;
            case SHOW_PEOPLE:
                rowsDeleted = db.delete(StContract.ShowEntry.PERSON_RELATION_TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case SHOWS:
                rowsUpdated = db.update(StContract.ShowEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case SEASONS:
                rowsUpdated = db.update(StContract.SeasonEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case EPISODES:
                rowsUpdated = db.update(StContract.EpisodeEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case COMMENTS:
                rowsUpdated = db.update(StContract.CommentEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case GENRES:
                rowsUpdated = db.update(StContract.GenreEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PEOPLE:
                rowsUpdated = db.update(StContract.PersonEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case SHOW_GENRES:
                rowsUpdated = db.update(StContract.ShowEntry.GENRE_RELATION_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case SHOW_PEOPLE:
                rowsUpdated = db.update(StContract.ShowEntry.PERSON_RELATION_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOWS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        // First try to update possible existing value in DB
                        int updated = db.update(StContract.ShowEntry.TABLE_NAME, value,
                                sShowSelection,
                                new String[]{value.getAsString(StContract.ShowEntry._ID)});
                        if (updated == 1) {
                            returnCount++;
                        } else {
                            long _id = db.insert(StContract.ShowEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case SEASONS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        // First try to update possible existing value in DB
                        int updated = db.update(StContract.SeasonEntry.TABLE_NAME, value,
                                sSeasonSelection,
                                new String[]{value.getAsString(StContract.SeasonEntry._ID)});
                        if (updated == 1) {
                            returnCount++;
                        } else {
                            long _id = db.insert(StContract.SeasonEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case EPISODES: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        // First try to update possible existing value in DB
                        int updated = db.update(StContract.EpisodeEntry.TABLE_NAME, value,
                                sEpisodeSelection,
                                new String[]{value.getAsString(StContract.EpisodeEntry._ID)});
                        if (updated == 1) {
                            returnCount++;
                        } else {
                            long _id = db.insert(StContract.EpisodeEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case COMMENTS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        // First try to update possible existing value in DB
                        int updated = db.update(StContract.CommentEntry.TABLE_NAME, value,
                                sCommentSelection,
                                new String[]{value.getAsString(StContract.CommentEntry._ID)});
                        if (updated == 1) {
                            returnCount++;
                        } else {
                            long _id = db.insert(StContract.CommentEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case GENRES: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        // First try to update possible existing value in DB
                        int updated = db.update(StContract.GenreEntry.TABLE_NAME, value,
                                sGenreSelection,
                                new String[]{value.getAsString(StContract.GenreEntry._ID)});
                        if (updated == 1) {
                            returnCount++;
                        } else {
                            long _id = db.insert(StContract.GenreEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case PEOPLE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        // First try to update possible existing value in DB
                        int updated = db.update(StContract.PersonEntry.TABLE_NAME, value,
                                sPersonSelection,
                                new String[]{value.getAsString(StContract.PersonEntry._ID)});
                        if (updated == 1) {
                            returnCount++;
                        } else {
                            long _id = db.insert(StContract.PersonEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case SHOW_GENRES: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        Cursor queryCursor = db.query(StContract.ShowEntry.GENRE_RELATION_TABLE_NAME,
                                null, sShowGenreSelection, new String[] {value.getAsString(StContract.ShowEntry.COLUMN_SHOW_ID),
                                        value.getAsString(StContract.ShowEntry.COLUMN_GENRE_ID)}, null, null, null);
                        if (queryCursor.getCount()==0) {
                            long _id = db.insert(StContract.ShowEntry.GENRE_RELATION_TABLE_NAME, null,
                                    value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                        queryCursor.close();
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case SHOW_PEOPLE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        Cursor queryCursor = db.query(StContract.ShowEntry.PERSON_RELATION_TABLE_NAME,
                                null, sShowPersonSelection, new String[] {value.getAsString(StContract.ShowEntry.COLUMN_SHOW_ID),
                                        value.getAsString(StContract.ShowEntry.COLUMN_PERSON_ID),
                                        value.getAsString(StContract.ShowEntry.COLUMN_CHARACTER)}, null, null, null);
                        if (queryCursor.getCount()==0) {
                            long _id = db.insert(StContract.ShowEntry.PERSON_RELATION_TABLE_NAME, null,
                                    value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                        queryCursor.close();
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }

    private Cursor getShowById(Uri uri, String[] projection, String sortOrder) {
        String showId = StContract.ShowEntry.getShowIdFromUri(uri);
        return sShowQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sShowSelection,
                new String[]{showId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getShows(String[] projection, String selection, String[] selectionArgs,
                             String sortOrder) {
        return sShowQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                Utility.MAX_SHOWS
        );
    }

    private Cursor getSeasonById(Uri uri, String[] projection, String sortOrder) {
        String seasonId = StContract.SeasonEntry.getSeasonIdFromUri(uri);
        return sSeasonQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sSeasonSelection,
                new String[]{seasonId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getSeasonsByShowId(Uri uri, String[] projection, String sortOrder) {
        String showId = StContract.SeasonEntry.getShowIdFromUri(uri);
        return sSeasonQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sSeasonByShowIdSelection,
                new String[]{showId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getSeasons(String[] projection, String selection, String[] selectionArgs,
                               String sortOrder) {
        return sSeasonQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getEpisodesBySeasonId(Uri uri, String[] projection, String sortOrder) {
        String seasonId = StContract.EpisodeEntry.getSeasonIdFromUri(uri);
        return sEpisodeQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sEpisodeBySeasonIdSelection,
                new String[]{seasonId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getEpisodeById(Uri uri, String[] projection, String sortOrder) {
        String episodeId = StContract.EpisodeEntry.getEpisodeIdFromUri(uri);
        return sEpisodeQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sEpisodeSelection,
                new String[]{episodeId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getEpisodes(String[] projection, String selection, String[] selectionArgs,
                              String sortOrder) {
        return sEpisodeQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCommentsByEpisodeId(Uri uri, String[] projection, String sortOrder) {
        String episodeId = StContract.CommentEntry.getEpisodeIdFromUri(uri);
        return sCommentQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sCommentByEpisodeIdSelection,
                new String[]{episodeId},
                null,
                null,
                sortOrder,
                Utility.MAX_COMMENTS
        );
    }

    private Cursor getCommentsByShowId(Uri uri, String[] projection, String sortOrder) {
        String showId = StContract.CommentEntry.getShowIdFromUri(uri);
        return sCommentQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sCommentByShowIdSelection,
                new String[]{showId},
                null,
                null,
                sortOrder,
                Utility.MAX_COMMENTS
        );
    }

    private Cursor getCommentById(Uri uri, String[] projection, String sortOrder) {
        String commentId = StContract.CommentEntry.getCommentIdFromUri(uri);
        return sCommentQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sCommentSelection,
                new String[]{commentId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getComments(String[] projection, String selection, String[] selectionArgs,
                               String sortOrder) {
        return sCommentQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getGenreById(Uri uri, String[] projection, String sortOrder) {
        String genreId = StContract.GenreEntry.getGenreIdFromUri(uri);
        return sGenreQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sGenreSelection,
                new String[]{genreId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getGenresByShowId(Uri uri, String[] projection, String sortOrder) {
        String showId = StContract.GenreEntry.getShowIdFromUri(uri);
        return sShowGenreQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sGenreByShowIdSelection,
                new String[]{showId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getGenres(String[] projection, String selection, String[] selectionArgs,
                            String sortOrder) {
        return sGenreQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getPersonById(Uri uri, String[] projection, String sortOrder) {
        String personId = StContract.PersonEntry.getPersonIdFromUri(uri);
        return sPersonQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sPersonSelection,
                new String[]{personId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getPeopleByShowId(Uri uri, String[] projection, String sortOrder) {
        String showId = StContract.PersonEntry.getShowIdFromUri(uri);
        return sShowPersonQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                sPersonByShowIdSelection,
                new String[]{showId},
                null,
                null,
                sortOrder,
                Utility.MAX_CAST_ENTRIES
        );
    }

    private Cursor getPeople(String[] projection, String selection, String[] selectionArgs,
                             String sortOrder) {
        return sPersonQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = StContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, StContract.PATH_SHOWS, SHOWS);
        matcher.addURI(authority, StContract.PATH_SHOWS + "/" + StContract.PATH_COMMENTS + "/#",
                SHOW_COMMENTS_WITH_ID);
        matcher.addURI(authority, StContract.PATH_SHOWS + "/" + StContract.PATH_SEASONS + "/#",
                SHOW_SEASONS_WITH_ID);
        matcher.addURI(authority, StContract.PATH_SHOWS + "/" + StContract.PATH_GENRES + "/#",
                SHOW_GENRES_WITH_ID);
        matcher.addURI(authority, StContract.PATH_SHOWS + "/" + StContract.PATH_PEOPLE + "/#",
                SHOW_PEOPLE_WITH_ID);
        matcher.addURI(authority, StContract.PATH_SHOWS + "/#", SHOW_WITH_ID);
        matcher.addURI(authority, StContract.PATH_SEASONS, SEASONS);
        matcher.addURI(authority, StContract.PATH_SEASONS + "/" + StContract.PATH_EPISODES + "/#",
                SEASON_EPISODES_WITH_ID);
        matcher.addURI(authority, StContract.PATH_SEASONS + "/#", SEASON_WITH_ID);
        matcher.addURI(authority, StContract.PATH_EPISODES, EPISODES);
        matcher.addURI(authority, StContract.PATH_EPISODES + "/" + StContract.PATH_COMMENTS + "/#",
                EPISODE_COMMENTS_WITH_ID);
        matcher.addURI(authority, StContract.PATH_EPISODES + "/#", EPISODE_WITH_ID);
        matcher.addURI(authority, StContract.PATH_COMMENTS, COMMENTS);
        matcher.addURI(authority, StContract.PATH_COMMENTS + "/#", COMMENT_WITH_ID);
        matcher.addURI(authority, StContract.PATH_GENRES, GENRES);
        matcher.addURI(authority, StContract.PATH_GENRES + "/#", GENRE_WITH_ID);
        matcher.addURI(authority, StContract.PATH_PEOPLE, PEOPLE);
        matcher.addURI(authority, StContract.PATH_PEOPLE + "/#", PERSON_WITH_ID);
        matcher.addURI(authority, StContract.PATH_SHOW_GENRES, SHOW_GENRES);
        matcher.addURI(authority, StContract.PATH_SHOW_GENRES + "/#", SHOW_GENRE_RELATION_WITH_ID);
        matcher.addURI(authority, StContract.PATH_SHOW_PEOPLE, SHOW_PEOPLE);
        matcher.addURI(authority, StContract.PATH_SHOW_PEOPLE + "/#", SHOW_PERSON_RELATION_WITH_ID);

        return matcher;
    }
}