package com.dawhey.mlij_blogapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Adapters.ChapterListAdapter;
import com.dawhey.mlij_blogapp.Filters.ChaptersTitleFilter;
import com.dawhey.mlij_blogapp.Listeners.OnChapterDownloadedListener;
import com.dawhey.mlij_blogapp.Listeners.OnResultsFilteredListener;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Bookmark;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Presenters.ChaptersListFragmentPresenter;
import com.dawhey.mlij_blogapp.R;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepositoryImpl;
import com.dawhey.mlij_blogapp.Views.ChaptersListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by dawhey on 17.03.17.
 */

public class ChaptersListFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        OnChapterDownloadedListener,
        SearchView.OnQueryTextListener,
        OnResultsFilteredListener,
        ChaptersListView {

    private static final int NO_RESULTS = 0;

    @BindView(R.id.chapters_list_view)
    RecyclerView chaptersListView;

    @BindView(R.id.swipe_refresh_chapters_view)
    SwipeRefreshLayout swipeRefreshView;

    @BindView(R.id.error_chapters_view)
    RelativeLayout errorView;

    @BindView(R.id.no_chapters_view)
    RelativeLayout noChaptersView;

    private ChaptersTitleFilter filter;
    private ChapterListAdapter chapterListAdapter;
    private ChaptersListFragmentPresenter presenter;
    private List<Chapter> chapters;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity().getApplicationContext();
        presenter = new ChaptersListFragmentPresenter(this, new ChaptersRepositoryImpl(context), AndroidSchedulers.mainThread());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chapters_list, container, false);
        ButterKnife.bind(this, root);
        setHasOptionsMenu(true);
        swipeRefreshView.setOnRefreshListener(this);
        swipeRefreshView.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        chaptersListView.setLayoutManager(new LinearLayoutManager(getContext()));
        chapterListAdapter = new ChapterListAdapter(getContext(), this);

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.fragment_chapters));
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chapters == null) {
            presenter.loadChapters();
        } else {
            showChapters(chapters);
        }
    }

    @Override
    public void onRefresh() {
        presenter.loadChapters();
    }

    /**
     * setting chapters list adapter and filter, then
     * showing chapters passed from presenter
     * @param chapters to show
     */
    @Override
    public void showChapters(List<Chapter> chapters) {
        errorView.setVisibility(View.GONE);
        swipeRefreshView.setRefreshing(false);
        chapterListAdapter.setPosts(chapters);
        chaptersListView.setAdapter(chapterListAdapter);
        if (getContext() != null) {
            chaptersListView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_faster));
        }
        filter = (ChaptersTitleFilter) chapterListAdapter.getFilter();
        filter.setOnResultsFilteredListener(ChaptersListFragment.this);
    }

    @Override
    public void showError() {
        swipeRefreshView.setRefreshing(false);
        if (chapters == null) {
            errorView.setVisibility(View.VISIBLE);
            if (getContext() != null) {
                errorView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up));
            }
        }
    }

    @Override
    public void showLoading() {
        errorView.setVisibility(View.GONE);
        swipeRefreshView.setRefreshing(true);
    }

    @Override
    public void showNoChapters() {
        noChaptersView.setVisibility(View.VISIBLE);
        noChaptersView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up_faster));
    }

    /**
     * Method used to update chapters from presenter if downloaded
     * @param chapters to update
     */
    @Override
    public void updateChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    /**
     * Cache the downloader chapter
     * @param chapter to cache
     */
    @Override
    public void onChapterDownloaded(Chapter chapter) {
        for (Chapter c : chapters) {
            if (c.getId().equals(chapter.getId())) {
                c.setContent(chapter.getContent());
            }
        }
    }

    /**
     * Setup search view listener
     * @param menu to create
     * @param inflater needed to create menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chapters_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_by_title));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Open the bookmark if it exists
     * @param item - choosing the bookmark item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PreferencesManager manager = PreferencesManager.getInstance(getContext());
        Bookmark bookmark = manager.getBookmark();
        if (bookmark != null) {
            openBookmark(manager, bookmark);
        } else {
            if (getView() != null) {
                Snackbar.make(getView(), getString(R.string.no_bookmark), Snackbar.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (chapters != null) {
            filter.filter(newText);
        }
        return false;
    }

    @Override
    public void onResultsFiltered(int count) {
        if (count == NO_RESULTS) {
            showNoChapters();
        } else {
            noChaptersView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    /**
     * Open selected bookmark
     * @param manager PreferencesManager to
     * @param bookmark to open
     */
    private void openBookmark(PreferencesManager manager, Bookmark bookmark) {
        manager.setLastChapter(bookmark.getChapter());
        ChapterFragment chapterFragment = new ChapterFragment();
        chapterFragment.setOnChapterDownloadedListener(this);
        chapterFragment.setOpenedFromBookmark(true);
        ((MainActivity) getActivity()).setCurrentVisibleFragment(ChapterFragment.class);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in_faster, R.anim.fade_out)
                .replace(R.id.content_main, chapterFragment, MainActivity.TAG_FRAGMENT_TO_RETAIN)
                .addToBackStack(null)
                .commit();
    }
}
