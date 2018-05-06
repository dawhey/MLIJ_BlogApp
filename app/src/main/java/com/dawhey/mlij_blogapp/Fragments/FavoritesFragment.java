package com.dawhey.mlij_blogapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Adapters.ChapterListAdapter;
import com.dawhey.mlij_blogapp.Adapters.ChapterListViewHolder;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.R;

import java.util.List;

/**
 * Created by dawhey on 17.03.17.
 */

public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";

    private Snackbar snackbar;
    private ChapterListAdapter favoriteListAdapter;
    private RecyclerView favoriteListView;
    private RelativeLayout noFavsView;
    private List<Chapter> favorites;
    private Chapter queuedToDelete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_favorites));

        noFavsView = (RelativeLayout) root.findViewById(R.id.no_favorites_view);
        favoriteListView = (RecyclerView) root.findViewById(R.id.favorites_list_view);
        favoriteListView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.SimpleCallback favoritesItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                queuedToDelete = favorites.get(viewHolder.getAdapterPosition());
                Snackbar snackbar = initializeRemoveSnackbar((ChapterListViewHolder) viewHolder, root);
                snackbar.show();
            }
        };

        ItemTouchHelper favoritesTouchHelper = new ItemTouchHelper(favoritesItemTouchCallback);
        favoritesTouchHelper.attachToRecyclerView(favoriteListView);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentVisibleFragment(FavoritesFragment.class);
        favorites = PreferencesManager.getInstance(getContext()).getFavoriteChapters();
        if (favorites != null && !favorites.isEmpty()) {
            showFavorites(favorites);
        } else {
            showNoFavsView();
        }
    }

    private void showFavorites(List<Chapter> favorites) {
        favoriteListAdapter = new ChapterListAdapter(getContext(), this);
        favoriteListAdapter.setPosts(favorites);
        noFavsView.setVisibility(View.GONE);
        favoriteListView.setAdapter(favoriteListAdapter);
        favoriteListView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_faster));
    }

    private void showNoFavsView() {
        noFavsView.setVisibility(View.VISIBLE);
        noFavsView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up));
    }

    private Snackbar initializeRemoveSnackbar(final ChapterListViewHolder vh, final View root) {
        final boolean[] delete = {true};
        String message = getString(R.string.Removed) + vh.chapterNumberView.getText() + getString(R.string.from_favorites);
        snackbar = Snackbar.make(root, message, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteListAdapter.notifyItemChanged(vh.getAdapterPosition());
                delete[0] = false;
                queuedToDelete = null;
            }
        }).setActionTextColor(getResources().getColor(R.color.colorAccent));

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (delete[0]) {
                    removeChapterFromFavorites(queuedToDelete);
                    favoriteListAdapter.notifyItemRemoved(vh.getAdapterPosition());
                    if (favorites.size() == 0 && getContext() != null) {
                        showNoFavsView();
                    }
                }
            }
        });

        return snackbar;
    }

    private void removeChapterFromFavorites(Chapter chapter) {
        PreferencesManager.getInstance(getContext()).removeFromFavorites(chapter);
        favorites.remove(chapter);
        queuedToDelete = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (queuedToDelete != null) {
            removeChapterFromFavorites(queuedToDelete);
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
    }
}
