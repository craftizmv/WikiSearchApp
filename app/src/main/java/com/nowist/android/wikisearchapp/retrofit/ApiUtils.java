package com.nowist.android.wikisearchapp.retrofit;

/**
 * Created by mayankverma on 22/07/17.
 */

public class ApiUtils {
    public static final String BASE_URL = "https://en.wikipedia.org/";

    public static RestApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(RestApiService.class);
    }

    public static RestApiService getApiService(String customUrl) {
        return RetrofitClient.getClient(customUrl).create(RestApiService.class);
    }
}
