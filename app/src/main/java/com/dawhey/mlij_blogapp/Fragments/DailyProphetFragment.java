package com.dawhey.mlij_blogapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Models.BlogEntry;
import com.dawhey.mlij_blogapp.Presenters.DailyProphetFragmentPresenter;
import com.dawhey.mlij_blogapp.R;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepositoryImpl;
import com.dawhey.mlij_blogapp.Views.DailyProphetView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by dawhe on 04.04.2017.
 */

public class DailyProphetFragment extends Fragment implements DailyProphetView {

    @BindView(R.id.prophet_content_view)
    RelativeLayout contentView;

    @BindView(R.id.error_prophet_view)
    RelativeLayout errorView;

    @BindView(R.id.prophet_progressbar)
    ProgressBar progressBar;

    @BindView(R.id.prophet_content_text)
    TextView prophetContentText;

    @BindView(R.id.error_refresh_button)
    FloatingActionButton refreshButton;

    private DailyProphetFragmentPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DailyProphetFragmentPresenter(new ChaptersRepositoryImpl(getContext()), this, AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_daily_prophet, container, false);
        ButterKnife.bind(this, root);
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.daily_prophet));
        }
        return root;
    }

    @OnClick(R.id.error_refresh_button)
    public void refresh() {
        presenter.loadContent();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadContent();
    }

    @Override
    public void showContent(BlogEntry content) {
        prophetContentText.setText(Html.fromHtml(content.getContent()));
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        contentView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_faster));
    }

    @Override
    public void showError() {
        progressBar.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up_faster));
    }

    @Override
    public void showLoading() {
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_faster));
    }
}
