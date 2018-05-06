package com.dawhey.mlij_blogapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Presenters.AboutBlogFragmentPresenter;
import com.dawhey.mlij_blogapp.R;
import com.dawhey.mlij_blogapp.Views.AboutBlogView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dawhey on 17.03.17.
 */

public class AboutBlogFragment extends Fragment implements View.OnClickListener, AboutBlogView{

    private AboutBlogFragmentPresenter presenter;

    @BindView(R.id.content_about)
    ScrollView contentView;

    @BindView(R.id.website_icon)
    ImageView websiteButton;

    @BindView(R.id.email_icon)
    ImageView emailButton;

    @BindView(R.id.facebook_icon)
    ImageView facebookButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new AboutBlogFragmentPresenter(this, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_blog, container, false);
        ButterKnife.bind(this, root);
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.fragment_about_blog));
        }
        websiteButton.setOnClickListener(this);
        emailButton.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        contentView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
    }

    @Override
    public void onClick(View view) {
        int clickedItemId = view.getId();

        switch (clickedItemId) {
            case R.id.website_icon:
                presenter.openBlogWebsite();
                break;
            case R.id.email_icon:
                presenter.sendEmail();
                break;
            case R.id.facebook_icon:
                presenter.openFacebookPage(getContext());
        }
    }

    @Override
    public void showSnackbar(int message, int length) {
        if (getView() != null) {
            Snackbar.make(getView(), message, length).show();
        }
    }
}
