package com.nowist.android.wikisearchapp.retrofit;

import android.content.Context;

import com.nowist.android.wikisearchapp.BuildConfig;
import com.nowist.android.wikisearchapp.Utils;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {

    private static Retrofit sRetrofitClient;

    static Retrofit getClient(String baseUrl, final Context applicationContext) {
        if (sRetrofitClient == null) {
            OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
            int cacheSize = 10 * 1024 * 1024; // 10 MB
            Cache cache = new Cache(applicationContext.getCacheDir(), cacheSize);
            okhttpBuilder.cache(cache);

            //Adding Logging Interceptor
            if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okhttpBuilder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (Utils.isNetworkAvailable(applicationContext)) {
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                        } else {
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
                        return chain.proceed(request);
                    }
                });

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

