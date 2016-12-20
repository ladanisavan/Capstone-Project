package com.nanodegree.android.showtime.api.tmdb;

import com.google.gson.annotations.SerializedName;

public class TvResult {
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("poster_path")
    private String posterPath;

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public String toString() {
        return "TvResult{" +
                "backdropPath='" + backdropPath + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
