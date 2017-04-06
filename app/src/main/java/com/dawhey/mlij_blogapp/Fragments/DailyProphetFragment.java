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
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Api.ApiManager;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.BlogEntry;
import com.dawhey.mlij_blogapp.Models.Bookmark;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dawhe on 04.04.2017.
 */

public class DailyProphetFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "DailyProphetFragment";

    private RelativeLayout contentView, errorView;
    private ProgressBar progressBar;
    private TextView prophetContentText;
    private FloatingActionButton refreshButton;

    private BlogEntry page;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_daily_prophet, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.daily_prophet));
        contentView = (RelativeLayout) root.findViewById(R.id.prophet_content_view);
        errorView = (RelativeLayout) root.findViewById(R.id.error_prophet_view);
        progressBar = (ProgressBar) root.findViewById(R.id.prophet_progressbar);
        prophetContentText = (TextView) root.findViewById(R.id.prophet_content_text);
        refreshButton = (FloatingActionButton) root.findViewById(R.id.error_refresh_button);
        refreshButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (page == null) {
            downloadChapterContent();
        } else {
            prophetContentText.setText(Html.fromHtml(page.getContent()));
            showContentView();
        }
    }

    private void downloadChapterContent() {
        showLoadingView();
        Call<BlogEntry> prophetCall = ApiManager.createBloggerService().getProphetPage();
        prophetCall.enqueue(new Callback<BlogEntry>() {
            @Override
            public void onResponse(Call<BlogEntry> call, Response<BlogEntry> response) {
                page = response.body();
                prophetContentText.setText(Html.fromHtml(page.getContent()));
                showContentView();
            }

            @Override
            public void onFailure(Call<BlogEntry> call, Throwable t) {
                showErrorView();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void showLoadingView() {
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_faster));
    }

    private void showContentView() {
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        contentView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_faster));
    }

    private void showErrorView() {
        progressBar.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up_faster));
    }

    @Override
    public void onClick(View v) {
        downloadChapterContent();
    }
}
