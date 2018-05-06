package com.dawhey.mlij_blogapp.Presenters;

import android.util.Log;

import com.dawhey.mlij_blogapp.Repositories.ChaptersRepository;
import com.dawhey.mlij_blogapp.Views.View;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class LcePresenter<V extends View, T> {

    protected ChaptersRepository model;
    protected V view;

    private Scheduler mainScheduler;
    private CompositeDisposable disposables;


    LcePresenter(ChaptersRepository model, V view, Scheduler mainScheduler) {
        this.model = model;
        this.view = view;
        this.mainScheduler = mainScheduler;
        disposables = new CompositeDisposable();
    }

    public void loadContent() {
        view.showLoading();
        disposables.add(provideContent().
                observeOn(mainScheduler)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<T>() {

                    @Override
                    public void onSuccess(T t) {
                        LcePresenter.this.onSuccess(t);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LcePresenter.this.onError(throwable);
                    }
                }));
    }


    public void unsubscribe() {
        disposables.clear();
    }

    void onSuccess(T t) {
        view.showContent(t);
    }

    void onError(Throwable throwable) {
        view.showError();
        Log.e(getClass().getName(), throwable.getMessage());
    }

    abstract Single<T> provideContent();

}
