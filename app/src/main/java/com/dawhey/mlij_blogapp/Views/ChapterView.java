package com.dawhey.mlij_blogapp.Views;

import com.dawhey.mlij_blogapp.Models.Chapter;

public interface ChapterView {

    void showLoading();

    void showError();

    void showContent(Chapter chapter);
}
