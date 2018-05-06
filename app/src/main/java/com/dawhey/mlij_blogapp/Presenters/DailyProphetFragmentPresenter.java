package com.dawhey.mlij_blogapp.Presenters;

import com.dawhey.mlij_blogapp.Models.BlogEntry;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepository;
import com.dawhey.mlij_blogapp.Views.DailyProphetView;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class DailyProphetFragmentPresenter extends LcePresenter<DailyProphetView, BlogEntry> {

    public DailyProphetFragmentPresenter(ChaptersRepository model, DailyProphetView view, Scheduler mainScheduler) {
        super(model, view, mainScheduler);
    }

    @Override
    Single<BlogEntry> provideContent() {
        return model.getProphetPage();
    }
}
