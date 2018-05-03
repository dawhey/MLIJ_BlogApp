package com.dawhey.mlij_blogapp.Repositories;

import android.content.Context;

import com.dawhey.mlij_blogapp.Api.ApiManager;
import com.dawhey.mlij_blogapp.Api.BloggerService;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Models.Posts;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class ChaptersRepositoryImpl implements ChaptersRepository {

    private BloggerService bloggerService;
    private PreferencesManager manager;

    public ChaptersRepositoryImpl(Context context) {
        this.bloggerService = ApiManager.createBloggerService();
        manager = PreferencesManager.getInstance(context);
    }

    @Override
    public Single<Posts> getAllChapters() {
        return bloggerService.listRepos();
    }

    @Override
    public void saveOldChapters(final List<Chapter> chapters) {
        List<String> chapterIds = new ArrayList<>();
        for (Chapter c : chapters) {
            chapterIds.add(c.getId());
        }
        manager.saveOldChapters(chapterIds);
    }
}
