package com.dawhey.mlij_blogapp.Presenters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;

import com.dawhey.mlij_blogapp.Fragments.AboutBlogFragment;
import com.dawhey.mlij_blogapp.R;
import com.dawhey.mlij_blogapp.Views.AboutBlogView;

public class AboutBlogFragmentPresenter {

    private static final String EMAIL_INTENT_TYPE = "message/rfc822";
    private static final String NEW_FACEBOOK_PAGE_URI_PREFIX = "fb://facewebmodal/f?href=";
    private static final String OLD_FACEBOOK_PAGE_URI_PREFIX = "fb://page/";
    private static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";

    private AboutBlogFragment fragment;
    private AboutBlogView view;

    public AboutBlogFragmentPresenter(AboutBlogFragment fragment, AboutBlogView view) {
        this.fragment = fragment;
        this.view = view;
    }

    public void openBlogWebsite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fragment.getString(R.string.blog_website_url)));
        fragment.startActivity(browserIntent);
    }

    public void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType(EMAIL_INTENT_TYPE);
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{fragment.getString(R.string.blog_contact_mail)});

        try {
            fragment.startActivity(Intent.createChooser(i, fragment.getString(R.string.send_mail)));
        } catch (android.content.ActivityNotFoundException ex) {
            view.showSnackbar(R.string.no_email_client, Snackbar.LENGTH_SHORT);
        }
    }

    public void openFacebookPage(Context context) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(context);
        facebookIntent.setData(Uri.parse(facebookUrl));
        fragment.startActivity(facebookIntent);
    }

    private String getFacebookPageURL(Context context) {
        String pageUrl = fragment.getString(R.string.facebook_page_url);
        String pageId = fragment.getString(R.string.facebook_page_id);
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
