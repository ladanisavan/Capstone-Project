package com.nanodegree.android.showtime;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.nanodegree.android.showtime.adapters.ViewPagerAdapter;
import com.nanodegree.android.showtime.data.StContract;
import com.nanodegree.android.showtime.data.StProvider;
import com.nanodegree.android.showtime.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EpisodeDetailFragment extends Fragment
        implements StDetailFragment, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_EPISODE_LOADER_ID = 9;

    private static final String[] EPISODE_COLUMNS = {
            StContract.EpisodeEntry._ID,
            StContract.EpisodeEntry.COLUMN_NUMBER,
            StContract.EpisodeEntry.COLUMN_SEASON_NUMBER,
            StContract.EpisodeEntry.COLUMN_TITLE,
            StContract.EpisodeEntry.COLUMN_SCREENSHOT_PATH,
            StContract.EpisodeEntry.COLUMN_WATCHED,
            StContract.EpisodeEntry.COLUMN_WATCHLIST
    };
    // These indices are tied to EPISODE_COLUMNS. If EPISODE_COLUMNS changes, these must change too.
    public static final int COL_ID = 0;
    public static final int COL_NUMBER = 1;
    public static final int COL_SEASON_NUMBER = 2;
    public static final int COL_TITLE = 3;
    public static final int COL_SCREENSHOT_PATH = 4;
    public static final int COL_WATCHED = 5;
    public static final int COL_WATCHLIST = 6;

    private Uri mUri;
    private String mEpisodeId;
    private String mEpisodeNumberAndTitle = "";
    private Unbinder mButterKnifeUnbinder;
    private boolean mIsWatched;
    private boolean mIsWatchlist;

    @BindView(R.id.episode_screenshot)
    ImageView mEpisodeScreenshot;
    @BindView(R.id.episode_detail_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.episode_detail_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.episode_detail_appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.episode_detail_collapsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.episode_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.episode_detail_menu_watched_item)
    FloatingActionButton mFabWatched;
    @BindView(R.id.episode_detail_menu_watchlist_item)
    FloatingActionButton mFabWatchlist;
    @BindView(R.id.episode_detail_root)
    CoordinatorLayout mRoot;

    public EpisodeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(Utility.DETAIL_URI_EXTRA_KEY);
            mEpisodeId = null;
            if (mUri!=null) {
                mEpisodeId = StContract.EpisodeEntry.getEpisodeIdFromUri(mUri);
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_episode_detail, container, false);
        mButterKnifeUnbinder = ButterKnife.bind(this, rootView);

        getLoaderManager().initLoader(DETAIL_EPISODE_LOADER_ID, null, this);

        ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
        mToolbar.setTitleMarginTop(params.height * -1);
        params.height = params.height * 2;
        mToolbar.setLayoutParams(params);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mCollapsingToolbarLayout != null) {
                        mCollapsingToolbarLayout.setTitle(mEpisodeNumberAndTitle);
                    }
                    isShow = true;
                } else if (isShow) {
                    if (mCollapsingToolbarLayout != null) {
                        mCollapsingToolbarLayout.setTitle("");
                    }
                    isShow = false;
                }
            }
        });

        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);

        mFabWatched
                .setOnClickListener(new WtaEpisodeFabOnClickListener(StContract.EpisodeEntry.COLUMN_WATCHED));
        mFabWatchlist
                .setOnClickListener(new WtaEpisodeFabOnClickListener(StContract.EpisodeEntry.COLUMN_WATCHLIST));

        mRoot.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mButterKnifeUnbinder!=null) {
            mButterKnifeUnbinder.unbind();
        }
    }

    public void hideDetailLayout() {
        mRoot.setVisibility(View.INVISIBLE);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle arguments = new Bundle();
        arguments.putParcelable(Utility.DETAIL_URI_EXTRA_KEY, mUri);
        Fragment tabFragment;
        tabFragment = new EpisodeInfoFragment();
        tabFragment.setArguments(arguments);
        adapter.addFragment(tabFragment, getString(R.string.episode_info_tab));
        tabFragment = new EpisodeCommentsFragment();
        tabFragment.setArguments(arguments);
        adapter.addFragment(tabFragment, getString(R.string.episode_comments_tab));

        mViewPager.setAdapter(adapter);
    }

    private void setWatchedFabStatus(boolean active) {
        int colorId = active ? R.color.colorAccent : R.color.stGray;
        mFabWatched.setColorNormal(ContextCompat.getColor(getActivity(), colorId));
        mIsWatched = active;
    }

    private void setWatchlistFabStatus(boolean active) {
        int colorId = active ? R.color.colorAccent : R.color.stGray;
        mFabWatchlist.setColorNormal(ContextCompat.getColor(getActivity(), colorId));
        mIsWatchlist = active;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            return new CursorLoader(getActivity(),
                    mUri, EPISODE_COLUMNS, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        onDetailEpisodeLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Do nothing
    }

    private void onDetailEpisodeLoadFinished(Cursor data) {

        mEpisodeNumberAndTitle = data.getString(COL_SEASON_NUMBER) + "x" +
                data.getString(COL_NUMBER) + ": " + data.getString(COL_TITLE);
        String screenshotPath = data.getString(COL_SCREENSHOT_PATH);
        if (screenshotPath!=null) {
            Glide.with(getActivity()).load(screenshotPath)
                    .crossFade().into(mEpisodeScreenshot);
        } else {
            mEpisodeScreenshot.setImageBitmap(null);
        }
        int watched = data.getInt(COL_WATCHED);
        setWatchedFabStatus(watched == 1);
        int watchlist = data.getInt(COL_WATCHLIST);
        setWatchlistFabStatus(watchlist == 1);

        mRoot.setVisibility(View.VISIBLE);
    }

    private class WtaEpisodeFabOnClickListener implements View.OnClickListener {

        private String mColumnName;

        public WtaEpisodeFabOnClickListener(String column) {
            mColumnName = column;
        }

        @Override
        public void onClick(View view) {
            int value = 0;
            if (mColumnName.equals(StContract.EpisodeEntry.COLUMN_WATCHED)) {
                mIsWatched = !mIsWatched;
                value = mIsWatched ? 1 : 0;
            } else if (mColumnName.equals(StContract.EpisodeEntry.COLUMN_WATCHLIST)) {
                mIsWatchlist = !mIsWatchlist;
                value = mIsWatchlist ? 1 : 0;
            }

            ContentValues episode = new ContentValues();
            episode.put(StContract.EpisodeEntry._ID, mEpisodeId);
            episode.put(mColumnName, value);
            getActivity().getContentResolver()
                    .update(StContract.EpisodeEntry.CONTENT_URI,
                            episode,
                            StProvider.sEpisodeSelection,
                            new String[]{mEpisodeId});
        }
    }
}
