package com.nanodegree.android.showtime.api.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TmdbSearchResult {
    @SerializedName("tv_results")
    private List<TvResult> tvResults;

    public List<TvResult> getTvResults() {
        return tvResults;
    }

    public void setTvResults(List<TvResult> tvResults) {
        this.tvResults = tvResults;
    }

    @Override

    public String toString() {
        return "TmdbSearchResult{" +
                "tvResults=" + tvResults +
                '}';
    }
}
