package com.dawhey.mlij_blogapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Adapters.ChapterListAdapter;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.R;

import java.util.List;

/**
 * Created by dawhey on 17.03.17.
 */

public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";

    private ChapterListAdapter favoriteListAdapter;
    private RecyclerView favoriteListView;
    private RelativeLayout noFavsView;
    private List<Chapter> favorites;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_favorites));

        noFavsView = (RelativeLayout) root.findViewById(R.id.no_favorites_view);
        favoriteListView = (RecyclerView) root.findViewById(R.id.favorites_list_view);
        favoriteListView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
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

}
