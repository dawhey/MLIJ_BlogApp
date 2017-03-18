package com.dawhey.mlij_blogapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.R;

/**
 * Created by dawhey on 17.03.17.
 */

public class AboutBlogFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_blog, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_about_blog));

        return root;
    }
}
