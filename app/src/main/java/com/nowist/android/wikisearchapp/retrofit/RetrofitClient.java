package com.nowist.android.wikisearchapp.retrofit;

import com.nowist.android.wikisearchapp.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit sRetrofitClient;

    public static Retrofit getClient(String baseUrl) {
        if (sRetrofitClient == null) {
            OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

            //Adding Logging Interceptor
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okhttpBuilder.addInterceptor(interceptor);

            }

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpBuilder.build());

            sRetrofitClient = retrofitBuilder.build();
        }
        return sRetrofitClient;
    }
}

