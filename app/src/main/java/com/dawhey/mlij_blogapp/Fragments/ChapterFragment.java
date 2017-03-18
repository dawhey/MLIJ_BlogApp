package com.dawhey.mlij_blogapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Item;
import com.dawhey.mlij_blogapp.R;

/**
 * Created by dawhey on 18.03.17.
 */

public class ChapterFragment extends Fragment {

    private TextView titleView;
    private Item item;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = PreferencesManager.getInstance(getContext()).getLastChapter();
        item.getId();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);
        titleView = (TextView) root.findViewById(R.id.chapter_title_view);
        titleView.setText(item.getTitleFormatted());
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(item.getChapterHeaderFormatted());
        return root;
    }
}
