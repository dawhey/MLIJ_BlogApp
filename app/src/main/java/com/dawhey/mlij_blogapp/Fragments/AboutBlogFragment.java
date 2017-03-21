package com.dawhey.mlij_blogapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.R;

/**
 * Created by dawhey on 17.03.17.
 */

public class AboutBlogFragment extends Fragment implements View.OnClickListener{

    private static final String EMAIL_INTENT_TYPE = "message/rfc822";
    private static final String NEW_FACEBOOK_PAGE_URI_PREFIX = "fb://facewebmodal/f?href=";
    private static final String OLD_FACEBOOK_PAGE_URI_PREFIX = "fb://page/";
    private static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";

    private ScrollView contentView;
    private ImageView websiteButton, emailButton, facebookButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_blog, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_about_blog));
        contentView = (ScrollView) root.findViewById(R.id.content_about);

        websiteButton = (ImageView) root.findViewById(R.id.website_icon);
        websiteButton.setOnClickListener(this);
        emailButton = (ImageView) root.findViewById(R.id.email_icon);
        emailButton.setOnClickListener(this);
        facebookButton = (ImageView) root.findViewById(R.id.facebook_icon);
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
                openBlogWebsite();
                break;
            case R.id.email_icon:
                sendEmail();
                break;
            case R.id.facebook_icon:
                openFacebookPage();
        }
    }

    private void openBlogWebsite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.blog_website_url)));
        startActivity(browserIntent);
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType(EMAIL_INTENT_TYPE);
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getString(R.string.blog_contact_mail)});

        try {
            startActivity(Intent.createChooser(i, getString(R.string.send_mail)));
        } catch (android.content.ActivityNotFoundException ex) {
            if (getView() != null) {
                Snackbar.make(getView(), R.string.no_email_client, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void openFacebookPage() {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(getContext());
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);
    }

    public String getFacebookPageURL(Context context) {
        String pageUrl = getString(R.string.facebook_page_url);
        String pageId = getString(R.string.facebook_page_id);
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo(FACEBOOK_PACKAGE_NAME, 0).versionCode;
            if (versionCode >= 3002850) {
                return NEW_FACEBOOK_PAGE_URI_PREFIX + pageUrl;
            } else {
                return OLD_FACEBOOK_PAGE_URI_PREFIX + pageId;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return pageUrl;
        }
    }
}
