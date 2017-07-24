package com.nowist.android.wikisearchapp.retrofit;

import android.content.Context;

public class ApiUtils {
    private static final String BASE_URL = "https://en.wikipedia.org/";

    public static RestApiService getApiService(Context applicationContext) {
        return RetrofitClient.getClient(BASE_URL, applicationContext).create(RestApiService.class);
    }

    public static RestApiService getApiService(String customUrl, Context applicationContext) {
        return RetrofitClient.getClient(customUrl, applicationContext).create(RestApiService.class);
    }
}
