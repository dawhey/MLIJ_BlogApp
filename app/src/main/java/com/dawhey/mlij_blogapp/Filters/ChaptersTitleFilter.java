package com.dawhey.mlij_blogapp.Filters;

import android.widget.Filter;

import com.dawhey.mlij_blogapp.Adapters.ChapterListAdapter;
import com.dawhey.mlij_blogapp.Models.Chapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dawhey on 23.03.17.
 */

public class ChaptersTitleFilter extends Filter {

    private OnResultsFilteredListener listener;
    private ChapterListAdapter chapterListAdapter;
    private List<Chapter> originalList;
    private List<Chapter> filteredList;

    public ChaptersTitleFilter(ChapterListAdapter chapterListAdapter, List<Chapter> originalList) {
        this.chapterListAdapter = chapterListAdapter;
        this.originalList = new LinkedList<>(originalList);
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (charSequence.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = charSequence.toString().toLowerCase().trim();
            for (Chapter chapter : originalList) {
                if (chapter.getTitle().toLowerCase().contains(filterPattern)) {
                    filteredList.add(chapter);
                }
            }
        }

        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        chapterListAdapter.setQueryText(charSequence.toString());
        chapterListAdapter.setPosts((List<Chapter>) filterResults.values);
        chapterListAdapter.notifyDataSetChanged();
        listener.onResultsFiltered(filterResults.count);
    }

    public void setOnResultsFilteredListener(OnResultsFilteredListener listener) {
        this.listener = listener;
    }

    public interface OnResultsFilteredListener {
        void onResultsFiltered(int count);
    }
}
