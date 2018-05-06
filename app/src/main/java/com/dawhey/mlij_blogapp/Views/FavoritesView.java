package com.dawhey.mlij_blogapp.Views;

import com.dawhey.mlij_blogapp.Adapters.ChapterListViewHolder;
import com.dawhey.mlij_blogapp.Models.Chapter;

import java.util.List;

public interface FavoritesView {
    void showNoFavoritesView();

    void showFavorites(List<Chapter> favorites);

    void showSnackbar();

    void initializeSnackbar(final ChapterListViewHolder vh);
}
