package com.dawhey.mlij_blogapp.Presenters;

import android.content.Context;
import android.util.Log;

import com.dawhey.mlij_blogapp.Application;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Models.Posts;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepository;
import com.dawhey.mlij_blogapp.Views.ChaptersListView;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChaptersListFragmentPresenter {

    private static final String TAG = "ChaptersListFragment";

    private ChaptersListView view;
    private ChaptersRepository model;
    private Scheduler mainScheduler;
    private CompositeDisposable disposables;


    public ChaptersListFragmentPresenter(ChaptersListView view, ChaptersRepository model, Scheduler mainScheduler) {
        this.view = view;
        this.model = model;
        this.mainScheduler = mainScheduler;
        disposables = new CompositeDisposable();
    }

    public void loadChapters() {
        view.showLoading();
        disposables.add(model.getAllChapters()
                .observeOn(mainScheduler)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Posts>() {

            @Override
            public void onSuccess(Posts posts) {
                List<Chapter> chapters = posts.getChapters();
                model.saveOldChapters(chapters);
                view.showChapters(chapters);
                view.updateChapters(chapters);
            }

            @Override
            public void onError(Throwable throwable) {
                showError(throwable);
            }
        }));
    }

    private void showError(Throwable t) {
        view.showError();
        Log.e(TAG, t.getMessage());
    }

    public void unsubscribe() {
        disposables.clear();
    }

}
