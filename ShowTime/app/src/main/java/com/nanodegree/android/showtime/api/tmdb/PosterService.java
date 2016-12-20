package com.nanodegree.android.showtime.api.tmdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PosterService {

    @GET("find/{tmdbId}?")
    Call<TmdbSearchResult> getPosters(@Path("tmdbId") int showId, @Query("api_key") String apiKey, @Query("external_source") String extSource);
}
