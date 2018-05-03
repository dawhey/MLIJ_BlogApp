package com.dawhey.mlij_blogapp.Presenters;

import android.content.Context;
import android.util.Log;

import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Models.Posts;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepository;
import com.dawhey.mlij_blogapp.Views.ChaptersListView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChaptersListFragmentPresenter {

    private static final String TAG = "ChaptersListFragment";

    private ChaptersListView view;
    private ChaptersRepository model;
    private Context context;

    private CompositeDisposable disposables;
    private PreferencesManager manager;

    public ChaptersListFragmentPresenter(ChaptersListView view, ChaptersRepository model, Context context) {
        this.view = view;
        this.model = model;
        this.context = context;

        disposables = new CompositeDisposable();
        manager = PreferencesManager.getInstance(context);
    }

    public void loadChapters() {
        view.showLoading();
        disposables.add(model.getAllChapters()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Posts>() {
            @Override
            public void onSuccess(Posts posts) {
                List<Chapter> chapters = posts.getChapters();
                if (manager.checkIfFirstRun()) {
                    model.saveOldChapters(chapters);
                }
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
