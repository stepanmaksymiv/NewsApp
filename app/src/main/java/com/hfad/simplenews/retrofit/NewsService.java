package com.hfad.simplenews.retrofit;

import com.hfad.simplenews.model.TopNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {

    @GET("top-headlines")
    Call<TopNews> getAllNews(
            @Query("country")String country,
            @Query("apiKey") String apiKey);

    @GET("everything")
    Call<TopNews> getNewsSearch(
            @Query("q")String keyword,
            @Query("language")String language,
            @Query("sortBy")String sortBy,
            @Query("apiKey")String apiKey
    );
}
