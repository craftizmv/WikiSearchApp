package com.nowist.android.wikisearchapp.retrofit;

import com.nowist.android.wikisearchapp.models.QueryResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RestApiService {

    @GET("/w/api.php")
    Call<QueryResponse> getWikiSuggestions(@QueryMap Map<String, String> options);
}
