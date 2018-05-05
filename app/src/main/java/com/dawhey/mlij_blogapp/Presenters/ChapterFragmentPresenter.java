package com.dawhey.mlij_blogapp.Presenters;

import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepository;
import com.dawhey.mlij_blogapp.Views.ChapterView;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class ChapterFragmentPresenter extends Presenter<ChapterView, Chapter> {

    private String chapterId;

    public ChapterFragmentPresenter(ChaptersRepository model, ChapterView view, Scheduler mainScheduler, String chapterId) {
        super(model, view, mainScheduler);
    }

    @Override
    Single<Chapter> provideContent() {
        return model.getChapter(chapterId);
    }
}
