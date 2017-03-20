package com.dawhey.mlij_blogapp.Api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dawhey on 17.03.17.
 */


public class ApiManager {

    //6867644820719577961
    private static final String API_BASE_URL = "https://www.googleapis.com/blogger/v3/blogs/5161262949432411129/";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static BloggerService createBloggerService() {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(BloggerService.class);
    }
}
