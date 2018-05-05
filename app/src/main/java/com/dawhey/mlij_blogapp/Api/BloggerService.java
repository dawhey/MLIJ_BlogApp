package com.dawhey.mlij_blogapp.Api;

import com.dawhey.mlij_blogapp.BuildConfig;
import com.dawhey.mlij_blogapp.Models.BlogEntry;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Models.Posts;

import java.util.Observable;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dawhey on 17.03.17.
 */

public interface BloggerService {

    @GET("posts?key=" + BuildConfig.BLOGGER_API_KEY + "&fields=items(id,url,published,title)&maxResults=500")
    Single<Posts> listRepos();

    @GET("posts/{id}?key=" + BuildConfig.BLOGGER_API_KEY + "&fields=id,url,published,title,content")
    Single<Chapter> getChapter(@Path("id") String id);

    @GET("pages/5695788482197599557?key=" + BuildConfig.BLOGGER_API_KEY)
    Call<BlogEntry> getProphetPage();
}
