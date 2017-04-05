package com.dawhey.mlij_blogapp.Listeners;

import com.dawhey.mlij_blogapp.Adapters.ChapterListAdapter;
import com.dawhey.mlij_blogapp.Models.Chapter;

/**
 * Created by dawhey on 19.03.17.
 */
public interface OnChapterClickListener {

    void onChapterItemClick(Chapter chapter, ChapterListAdapter.ViewHolder holder);
}