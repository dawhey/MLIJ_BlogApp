package com.dawhey.mlij_blogapp.Presenters;

import com.dawhey.mlij_blogapp.Models.Posts;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepository;
import com.dawhey.mlij_blogapp.Views.ChaptersListView;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class ChaptersListFragmentPresenter extends LcePresenter<ChaptersListView, Posts> {


    public ChaptersListFragmentPresenter(ChaptersRepository model, ChaptersListView view, Scheduler mainScheduler) {
        super(model, view, mainScheduler);
    }

    @Override
    Single<Posts> provideContent() {
        return model.getAllChapters();
    }

    @Override
    void onSuccess(Posts posts) {
        super.onSuccess(posts);
        model.saveOldChapters(posts);
        view.showContent(posts);
        view.updateChapters(posts);
    }
}
