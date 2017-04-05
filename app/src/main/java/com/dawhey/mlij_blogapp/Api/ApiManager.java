package com.dawhey.mlij_blogapp.Api;

import com.dawhey.mlij_blogapp.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dawhey on 17.03.17.
 */


public class ApiManager {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BuildConfig.BLOGGER_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static BloggerService createBloggerService() {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(BloggerService.class);
    }
}
