package com.dawhey.mlij_blogapp.Api;

import com.dawhey.mlij_blogapp.BuildConfig;
import com.dawhey.mlij_blogapp.Models.Posts;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dawhey on 17.03.17.
 */

public interface BloggerService {

    @GET("posts?key=" + BuildConfig.BLOGGER_API_KEY + "&fields=items(id,url,published,title)&maxResults=500")
    Call<Posts> listRepos();

}
