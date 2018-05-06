package com.dawhey.mlij_blogapp.Presenters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;

import com.dawhey.mlij_blogapp.Listeners.OnChangeFontSizeListener;
import com.dawhey.mlij_blogapp.Listeners.OnChapterDownloadedListener;
import com.dawhey.mlij_blogapp.Models.Bookmark;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.R;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepository;
import com.dawhey.mlij_blogapp.Utils.FirebaseLogger;
import com.dawhey.mlij_blogapp.Views.ChapterView;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public class ChapterFragmentPresenter extends LcePresenter<ChapterView, Chapter> {

    private String chapterId;
    private OnChapterDownloadedListener listener;

    private int firstVisibleCharacterOffset;

    public ChapterFragmentPresenter(ChaptersRepository model, ChapterView view, Scheduler mainScheduler, OnChapterDownloadedListener listener) {
        super(model, view, mainScheduler);
        this.chapterId = model.getLastChapter().getId();
        this.listener = listener;
    }

    @Override
    Single<Chapter> provideContent() {
        return model.getChapter(chapterId);
    }

    @Override
    void onSuccess(Chapter chapter) {
        model.setLastChapter(chapter);
        if (listener != null) {
            listener.onChapterDownloaded(chapter);
        }
        if (firstVisibleCharacterOffset != 0) {
            view.scrollToPixelOffset(firstVisibleCharacterOffset);
        }

        if (view.isOpenedFromBookmark()) {
            Bookmark bookmark = model.getBookmark();
            view.scrollToPixelOffset(bookmark.getPosition());
            view.showSnackbar(R.string.opened_from_bookmark, Snackbar.LENGTH_SHORT);
            view.setOpenedFromBookmark(false);
        }
        super.onSuccess(chapter);
    }

    public void handleFavoriteClick(Chapter chapter, Context context) {
        if (model.isInFavorites(chapter)) {
            model.removeFromFavorites(chapter);
            view.showSnackbar(R.string.deleted_from_favorites_message, Snackbar.LENGTH_SHORT);
        } else {
            model.addToFavorites(chapter);
            view.showSnackbar(R.string.added_to_favorites_message, Snackbar.LENGTH_SHORT);
            FirebaseLogger.getInstance(context).logAddingToFavoritesEvent(chapter);
        }
        setupFavoriteButton(chapter);
    }

    public void setupFavoriteButton(Chapter chapter) {
        if (model.isInFavorites(chapter)) {
            view.updateFavoriteButton(R.color.colorAccent);
        } else {
            view.updateFavoriteButton(R.color.textDefault);
        }
    }

    public void saveBookmark(Chapter chapter, Context context, int scrollOffset) {
        Bookmark bookmark = new Bookmark(scrollOffset, chapter);
        model.saveBookmark(bookmark);
        view.showSnackbar(R.string.saved_bookmark, Snackbar.LENGTH_SHORT);
        FirebaseLogger.getInstance(context).logPlaceBookmark(chapter);
    }

    public void changeFontSize(OnChangeFontSizeListener fontSizeListener) {
        if (fontSizeListener.getChangedFontSize() != fontSizeListener.getFontSize()) {
            view.updateFontSize(fontSizeListener.getChangedFontSize());
            model.setChapterFontSize(fontSizeListener.getChangedFontSize());
            view.showSnackbar(R.string.default_fontsize_changed, Snackbar.LENGTH_LONG);
            fontSizeListener.setChangedFontSize(fontSizeListener.getChangedFontSize());
        }
        view.hideFontSliderView();
    }

    public void setFirstVisibleCharacterOffset(int firstVisibleCharacterOffset) {
        this.firstVisibleCharacterOffset = firstVisibleCharacterOffset;
        view.scrollToPixelOffset(firstVisibleCharacterOffset);
    }

    public Chapter getLastChapter() {
        return model.getLastChapter();
    }
}
