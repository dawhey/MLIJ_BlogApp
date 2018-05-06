package com.dawhey.mlij_blogapp.Views;

import com.dawhey.mlij_blogapp.Models.Chapter;

public interface ChapterView extends View<Chapter> {

    void showSnackbar(int message, int length);

    void updateFavoriteButton(int color);

    void showFontSliderView();

    void hideFontSliderView();

    void updateFontSize(int fontSize);

    void scrollToPixelOffset(int position);

    boolean isOpenedFromBookmark();

    void setOpenedFromBookmark(boolean openedFromBookmark);

}
