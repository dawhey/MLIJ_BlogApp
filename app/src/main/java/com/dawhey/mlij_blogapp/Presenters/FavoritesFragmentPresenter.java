package com.dawhey.mlij_blogapp.Presenters;

import com.dawhey.mlij_blogapp.Adapters.ChapterListViewHolder;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepositoryImpl;
import com.dawhey.mlij_blogapp.Views.FavoritesView;

import java.util.List;

public class FavoritesFragmentPresenter {

    private FavoritesView view;
    private ChaptersRepositoryImpl model;
    private List<Chapter> favorites;
    private Chapter queuedToDelete;

    public FavoritesFragmentPresenter(FavoritesView view, ChaptersRepositoryImpl model) {
        this.view = view;
        this.model = model;
    }

    public void loadFavorites() {
        favorites = model.getFavoriteChapters();
        if (favorites != null && !favorites.isEmpty()) {
            view.showFavorites(favorites);
        } else {
            view.showNoFavoritesView();
        }
    }

    public void removeChapterFromFavorites() {
        favorites.remove(queuedToDelete);
        model.removeFromFavorites(queuedToDelete);
        this.queuedToDelete = null;
    }

    public List<Chapter> getFavorites() {
        return favorites;
    }

    public void handleSwipeToDelete(int adapterPosition, ChapterListViewHolder viewHolder) {
        queuedToDelete = favorites.get(adapterPosition);
        view.initializeSnackbar(viewHolder);
        view.showSnackbar();
    }

    public FavoritesView getView() {
        return view;
    }

    public Chapter getQueuedToDelete() {
        return queuedToDelete;
    }

    public void setQueuedToDelete(Chapter queuedToDelete) {
        this.queuedToDelete = queuedToDelete;
    }
}
