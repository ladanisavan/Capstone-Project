package com.nanodegree.android.showtime.network;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.nanodegree.android.showtime.R;
import com.nanodegree.android.showtime.SearchResultsActivity;
import com.nanodegree.android.showtime.ShowsFragment;
import com.nanodegree.android.showtime.api.tmdb.PosterService;
import com.nanodegree.android.showtime.api.trakt.TraktService;
import com.nanodegree.android.showtime.data.StContract;
import com.nanodegree.android.showtime.util.Utility;

import java.util.List;

/**
 * An Async Task in order to retrieve Trakt shows data according to provided keywords search results
 */
public class SearchByKeywordsAsyncTask extends AsyncTask<Object, Void, List<Integer>> {

    private final String LOG_TAG = SearchByKeywordsAsyncTask.class.getSimpleName();

    private Context mContext;
    private Boolean mNewActivity;
    private String mSearchText;

    public SearchByKeywordsAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected List<Integer> doInBackground(Object... params) {

        Log.d(LOG_TAG, "AsyncTask started");

        List<Integer> result = null;
        mNewActivity = (Boolean) params[0];
        mSearchText = (String) params[1];
        Integer year = null;
        if (params.length>2) {
            year = (Integer) params[2];
        }
        if ((mSearchText==null) || (mSearchText.isEmpty())) {
            return null;
        }

        try {
            TraktService traktService = Utility.getTraktService();
            PosterService posterService = Utility.getPosterService();

            // First delete last search result marks and scores
            ContentValues updateValues = new ContentValues();
            updateValues.put(StContract.ShowEntry.COLUMN_LAST_SEARCH_RESULT, 0);
            updateValues.put(StContract.ShowEntry.COLUMN_SEARCH_SCORE, 0.0);
            mContext.getContentResolver().update(StContract.ShowEntry.CONTENT_URI, updateValues,
                    null, null);

            result = Utility.synchronizeShowsByKeywordsData(mContext, LOG_TAG, traktService, posterService, mSearchText, year);

            Log.d(LOG_TAG, "AsyncTask correctly ended");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage(), e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Integer> result) {
        if (mNewActivity) {
            Intent intent = new Intent(mContext, SearchResultsActivity.class);
            intent.putExtra(Utility.SEARCH_KEYWORDS_EXTRA_KEY, mSearchText);
            mContext.startActivity(intent);

            SynchronizeShowsDetailsAsyncTask detailsTask = new SynchronizeShowsDetailsAsyncTask(mContext);
            detailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, result);
        } else if (mContext instanceof SearchResultsActivity) {
            ShowsFragment showsFragment = (ShowsFragment)((SearchResultsActivity)mContext).getSupportFragmentManager()
                    .findFragmentById(R.id.shows_container);
            showsFragment.updateSwipeRefreshLayout(Boolean.FALSE);
        }
    }
}
