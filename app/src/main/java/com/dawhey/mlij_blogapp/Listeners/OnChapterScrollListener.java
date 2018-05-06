package com.dawhey.mlij_blogapp.Listeners;

import android.support.design.widget.FloatingActionButton;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

public class OnChapterScrollListener implements ViewTreeObserver.OnScrollChangedListener {

    public static final int CONTENT_BEGINNING = 0;
    private int scrollPosition = 0;

    private FloatingActionButton favoriteButton;
    private ScrollView scrollView;


    public OnChapterScrollListener(FloatingActionButton favoriteButton, ScrollView scrollView) {
        this.favoriteButton = favoriteButton;
        this.scrollView = scrollView;
    }

    @Override
    public void onScrollChanged() {
        int newScrollPosition = scrollView.getScrollY();
        if (newScrollPosition > CONTENT_BEGINNING && newScrollPosition > scrollPosition) {
            favoriteButton.hide();
        } else {
            favoriteButton.show();
        }
        scrollPosition = newScrollPosition;
    }
}
