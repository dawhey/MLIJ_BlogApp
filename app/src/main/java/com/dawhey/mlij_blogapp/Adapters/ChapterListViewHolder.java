package com.dawhey.mlij_blogapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChapterListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.chapter_title_view)
    public TextView chapterTitleView;

    @BindView(R.id.chapter_number_view)
    public TextView chapterNumberView;

    @BindView(R.id.chapter_new_label)
    public TextView chapterNewView;

    @BindView(R.id.chapter_favourites_icon)
    public ImageView favoriteIconView;

    ChapterListViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}
