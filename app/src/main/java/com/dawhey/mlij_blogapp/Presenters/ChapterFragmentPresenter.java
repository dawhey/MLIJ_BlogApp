package com.dawhey.mlij_blogapp.Presenters;

import com.dawhey.mlij_blogapp.Listeners.OnChapterDownloadedListener;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepository;
import com.dawhey.mlij_blogapp.Views.ChapterView;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class ChapterFragmentPresenter extends Presenter<ChapterView, Chapter> {

    private String chapterId;
    private OnChapterDownloadedListener listener;

    public ChapterFragmentPresenter(ChaptersRepository model, ChapterView view, Scheduler mainScheduler, String chapterId, OnChapterDownloadedListener listener) {
        super(model, view, mainScheduler);
        this.chapterId = chapterId;
        this.listener = listener;
    }

    @Override
    Single<Chapter> provideContent() {
        return model.getChapter(chapterId);
    }

    @Override
    void onSuccess(Chapter chapter) {
        super.onSuccess(chapter);
        if (listener != null) {
            listener.onChapterDownloaded(chapter);
        }
    }
}
