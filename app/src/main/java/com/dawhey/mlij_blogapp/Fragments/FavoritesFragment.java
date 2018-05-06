package com.dawhey.mlij_blogapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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
import com.dawhey.mlij_blogapp.Listeners.OnSwipeFavoriteListener;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Presenters.FavoritesFragmentPresenter;
import com.dawhey.mlij_blogapp.R;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepositoryImpl;
import com.dawhey.mlij_blogapp.Views.FavoritesView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dawhey on 17.03.17.
 */

public class FavoritesFragment extends Fragment implements FavoritesView {

    private Snackbar snackbar;
    private ChapterListAdapter favoriteListAdapter;
    private FavoritesFragmentPresenter presenter;
    private View root;

    @BindView(R.id.favorites_list_view)
    RecyclerView favoriteListView;

    @BindView(R.id.no_favorites_view)
    RelativeLayout noFavoritesView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FavoritesFragmentPresenter(this, new ChaptersRepositoryImpl(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, root);
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.fragment_favorites));
        }
        favoriteListView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper favoritesTouchHelper = new ItemTouchHelper(new OnSwipeFavoriteListener(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, presenter));
        favoritesTouchHelper.attachToRecyclerView(favoriteListView);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentVisibleFragment(FavoritesFragment.class);
        presenter.loadFavorites();
    }

    @Override
    public void showNoFavoritesView() {
        noFavoritesView.setVisibility(View.VISIBLE);
        noFavoritesView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up));
    }

    @Override
    public void showFavorites(List<Chapter> favorites) {
        favoriteListAdapter = new ChapterListAdapter(getContext(), this);
        favoriteListView.setAdapter(favoriteListAdapter);
        favoriteListAdapter.setPosts(favorites);
        noFavoritesView.setVisibility(View.GONE);
        favoriteListView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_faster));
    }

    @Override
    public void showSnackbar() {
        snackbar.show();
    }

    @Override
    public void initializeSnackbar(final ChapterListViewHolder vh) {
        final boolean[] delete = {true};
        String message = getString(R.string.Removed) + vh.chapterNumberView.getText() + getString(R.string.from_favorites);
        snackbar = Snackbar.make(root, message, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteListAdapter.notifyItemChanged(vh.getAdapterPosition());
                delete[0] = false;
                presenter.setQueuedToDelete(null);
            }
        }).setActionTextColor(getResources().getColor(R.color.colorAccent));

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (delete[0]) {
                    presenter.removeChapterFromFavorites();
                    favoriteListAdapter.notifyItemRemoved(vh.getAdapterPosition());
                    if (presenter.getFavorites().size() == 0 && getContext() != null) {
                        showNoFavoritesView();
                    }
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter.getQueuedToDelete() != null) {
            presenter.removeChapterFromFavorites();
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
    }
}
