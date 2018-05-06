package com.dawhey.mlij_blogapp.Listeners;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.dawhey.mlij_blogapp.Adapters.ChapterListViewHolder;
import com.dawhey.mlij_blogapp.Presenters.FavoritesFragmentPresenter;

public class OnSwipeFavoriteListener extends ItemTouchHelper.SimpleCallback {

    private FavoritesFragmentPresenter presenter;


    public OnSwipeFavoriteListener(int dragDirs, int swipeDirs, FavoritesFragmentPresenter presenter) {
        super(dragDirs, swipeDirs);
        this.presenter = presenter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        presenter.handleSwipeToDelete(viewHolder.getAdapterPosition(), (ChapterListViewHolder) viewHolder);
    }
}
