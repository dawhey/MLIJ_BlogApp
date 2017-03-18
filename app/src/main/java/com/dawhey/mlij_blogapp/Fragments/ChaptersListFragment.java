package com.dawhey.mlij_blogapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Adapters.ChapterListAdapter;
import com.dawhey.mlij_blogapp.Api.ApiManager;
import com.dawhey.mlij_blogapp.Models.Posts;
import com.dawhey.mlij_blogapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dawhey on 17.03.17.
 */

public class ChaptersListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ChaptersListFragment";

    private RecyclerView chaptersListView;
    private SwipeRefreshLayout swipeRefreshView;
    private RelativeLayout errorView;
    Posts posts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chapters_list, container, false);
        swipeRefreshView = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_chapters_view);
        swipeRefreshView.setOnRefreshListener(this);
        swipeRefreshView.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        errorView = (RelativeLayout) root.findViewById(R.id.error_chapters_view);
        chaptersListView = (RecyclerView) root.findViewById(R.id.chapters_list_view);
        chaptersListView.setLayoutManager(new LinearLayoutManager(getContext()));

        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_chapters));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (posts == null) {
            downloadChaptersList();
            swipeRefreshView.setRefreshing(true);
        } else {
            chaptersListView.setAdapter(new ChapterListAdapter(posts.getItems()));
        }
    }

    private void downloadChaptersList() {
        Call<Posts> postsCall = ApiManager.createBloggerService().listRepos();
        postsCall.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                swipeRefreshView.setRefreshing(false);
                if (response != null) {
                    posts = response.body();
                    chaptersListView.setAdapter(new ChapterListAdapter(posts.getItems()));
                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                swipeRefreshView.setRefreshing(false);
                if (posts == null) {
                    errorView.setVisibility(View.VISIBLE);
                    errorView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
                }
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onRefresh() {
        downloadChaptersList();
        errorView.setVisibility(View.GONE);
    }
}
