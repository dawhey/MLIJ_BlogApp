package com.dawhey.mlij_blogapp.Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Api.ApiManager;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Bookmark;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dawhey on 18.03.17.
 */

public class ChapterFragment extends Fragment implements ViewTreeObserver.OnScrollChangedListener{

    private static final String FIRST_VISIBLE_CHAR_KEY = "firstVisibleChar";
    private static final String TAG = "ChapterFragment";

    private int scrollPosition = 0;

    private boolean openedFromBookmark;
    private Chapter chapter;

    private OnChapterDownloadedListener listener;
    private RelativeLayout errorView;
    private ScrollView scrollView;
    private FloatingActionButton refreshButton;
    private ProgressBar progressBar;
    private TextView contentView;
    private FloatingActionButton favoriteButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chapter = PreferencesManager.getInstance(getContext()).getLastChapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(chapter.getChapterHeaderFormatted());
        initViews(root);
        initListeners();
        setFavoriteButton();

        return root;
    }

    private void initViews(View root) {
        TextView titleView = (TextView) root.findViewById(R.id.chapter_title_view);
        View dividerLine = root.findViewById(R.id.title_divider);
        dividerLine.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        contentView = (TextView) root.findViewById(R.id.chapter_content_view);
        errorView = (RelativeLayout) root.findViewById(R.id.error_chapter_view);
        scrollView = (ScrollView) root.findViewById(R.id.text_scroll_view);
        refreshButton = (FloatingActionButton) root.findViewById(R.id.error_refresh_button);
        progressBar = (ProgressBar) root.findViewById(R.id.chapter_progressbar);
        favoriteButton = (FloatingActionButton) root.findViewById(R.id.favorite_button);
        titleView.setText(chapter.getTitleFormatted());
    }

    private void initListeners() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadChapterContent();
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferencesManager manager = PreferencesManager.getInstance(getContext());
                favoriteButton.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up));
                if (manager.isInFavorites(chapter)) {
                    manager.removeFromFavorites(chapter);
                    favoriteButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textDefault)));
                    Snackbar.make(favoriteButton, R.string.deleted_from_favorites_message, Snackbar.LENGTH_SHORT).show();
                } else {
                    manager.addToFavorites(chapter);
                    favoriteButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    Snackbar.make(favoriteButton, R.string.added_to_favorites_message, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (chapter.getContent() == null) {
            downloadChapterContent();
        } else {
            contentView.setText(Html.fromHtml(chapter.getContent()));
            showContentView();
        }
    }

    private void downloadChapterContent() {
        showLoadingView();
        Call<Chapter> chapterCall = ApiManager.createBloggerService().getChapter(chapter.getId());
        chapterCall.enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                chapter = response.body();
                listener.onChapterDownloaded(chapter);
                PreferencesManager.getInstance(getContext()).setLastChapter(chapter);
                contentView.setText(Html.fromHtml(chapter.getContent()));
                showContentView();
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
                showErrorView();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void showLoadingView() {
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showContentView() {
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        favoriteButton.show();
        animateContent();

        if (openedFromBookmark) {
            Bookmark bookmark = PreferencesManager.getInstance(getContext()).getBookmark();
            scrollToPixelOffset(bookmark.getPosition());
            Snackbar.make(favoriteButton, R.string.opened_from_bookmark, Snackbar.LENGTH_SHORT).show();
            openedFromBookmark = false;
        }
    }

    private void animateContent()
    {
        if (getContext() != null) {
            Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            a.reset();
            contentView.clearAnimation();
            contentView.startAnimation(a);
            favoriteButton.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up));
        }
    }

    private void setFavoriteButton() {
        if (PreferencesManager.getInstance(getContext()).isInFavorites(chapter)) {
            favoriteButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        } else {
            favoriteButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textDefault)));
        }
    }

    private void showErrorView() {
        progressBar.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScrollChanged() {
        int newScrollPosition = scrollView.getScrollY();
        if (newScrollPosition > 0 && newScrollPosition > scrollPosition) {
            favoriteButton.hide();
        } else {
            favoriteButton.show();
        }
        scrollPosition = newScrollPosition;
    }

    public void setOnChapterDownloadedListener(OnChapterDownloadedListener listener) {
        this.listener = listener;
    }

    public interface OnChapterDownloadedListener {
        void onChapterDownloaded(Chapter chapter);
    }

    public boolean isOpenedFromBookmark() {
        return openedFromBookmark;
    }

    public void setOpenedFromBookmark(boolean openedFromBookmark) {
        this.openedFromBookmark = openedFromBookmark;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            final int firstVisibleCharacterOffset = savedInstanceState.getInt(FIRST_VISIBLE_CHAR_KEY);
            scrollToPixelOffset(firstVisibleCharacterOffset);
        }
    }

    private void scrollToPixelOffset(final int firstVisibleCharacterOffset) {
        scrollView.post(new Runnable() {
            public void run() {
                final int firstVisibleLineOffset = contentView.getLayout().getLineForOffset(firstVisibleCharacterOffset);
                final int pixelOffset = contentView.getLayout().getLineTop(firstVisibleLineOffset);
                scrollView.scrollTo(0, pixelOffset);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int firstVisibleCharacterOffset = getFirstVisibleCharacterOffset();
        outState.putInt(FIRST_VISIBLE_CHAR_KEY, firstVisibleCharacterOffset);
    }

    private int getFirstVisibleCharacterOffset() {
        final int firstVisibleLineOffset = contentView.getLayout().getLineForVertical(scrollView.getScrollY());
        return contentView.getLayout().getLineStart(firstVisibleLineOffset);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chapter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_last_read) {
            Bookmark bookmark = new Bookmark(getFirstVisibleCharacterOffset(), chapter);
            PreferencesManager.getInstance(getContext()).saveBookmark(bookmark);
            Snackbar.make(favoriteButton, R.string.saved_bookmark, Snackbar.LENGTH_SHORT).show();
        }

        return false;
    }
}