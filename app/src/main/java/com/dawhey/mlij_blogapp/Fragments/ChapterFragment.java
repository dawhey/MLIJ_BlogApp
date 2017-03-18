package com.dawhey.mlij_blogapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Api.ApiManager;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dawhey on 18.03.17.
 */

public class ChapterFragment extends Fragment implements ViewTreeObserver.OnScrollChangedListener{

    private static final String TAG = "ChapterFragment";

    private int scrollPosition = 0;

    private Chapter chapter;

    private View dividerLine;
    private TextView titleView;
    private RelativeLayout errorView;
    private ScrollView scrollView;
    private Button refreshButton;
    private ProgressBar progressBar;
    private TextView contentView;
    private FloatingActionButton favoriteButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chapter = PreferencesManager.getInstance(getContext()).getLastChapter();
        chapter.getId();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);
        titleView = (TextView) root.findViewById(R.id.chapter_title_view);
        contentView = (TextView) root.findViewById(R.id.chapter_content_view);
        dividerLine = root.findViewById(R.id.title_divider);
        dividerLine.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        errorView = (RelativeLayout) root.findViewById(R.id.error_chapter_view);
        scrollView = (ScrollView) root.findViewById(R.id.text_scroll_view);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);
        refreshButton = (Button) root.findViewById(R.id.error_refresh_button);
        progressBar = (ProgressBar) root.findViewById(R.id.chapter_progressbar);
        favoriteButton = (FloatingActionButton) root.findViewById(R.id.favorite_button);
        titleView.setText(chapter.getTitleFormatted());
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(chapter.getChapterHeaderFormatted());

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadChapterContent();
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(favoriteButton, "Dodano rozdział do ulubionych!", Snackbar.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chapter.getContent() == null) {
            downloadChapterContent();
        }
    }

    private void downloadChapterContent() {
        showLoadingView();
        Call<Chapter> chapterCall = ApiManager.createBloggerService().getChapter(chapter.getId());
        chapterCall.enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                chapter = response.body();
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
}
