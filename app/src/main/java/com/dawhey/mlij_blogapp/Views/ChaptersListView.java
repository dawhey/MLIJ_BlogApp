package com.dawhey.mlij_blogapp.Views;

import com.dawhey.mlij_blogapp.Models.Chapter;

import java.util.List;

public interface ChaptersListView {

    void showChapters(List<Chapter> chapters);

    void showError();

    void showLoading();

    void showNoChapters();

    void updateChapters(List<Chapter> chapters);
}
