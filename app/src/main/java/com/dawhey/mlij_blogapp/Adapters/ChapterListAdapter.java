package com.dawhey.mlij_blogapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Models.Item;
import com.dawhey.mlij_blogapp.R;
import java.util.List;

/**
 * Created by dawhey on 17.03.17.
 */
public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder> {

    private List<Item> posts;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chapterTitleView, chapterNumberView;
        public ImageView favoriteIconView;

        public ViewHolder(View v) {
            super(v);
            chapterTitleView = (TextView) v.findViewById(R.id.chapter_title_view);
            chapterNumberView = (TextView) v.findViewById(R.id.chapter_number_view);
            favoriteIconView = (ImageView) v.findViewById(R.id.chapter_favourites_icon);
        }
    }

    public ChapterListAdapter(List<Item> items) {
        posts = items;
    }

    @Override
    public ChapterListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Item post = posts.get(position);
        String[] titles = post.getTitle().split(": ");

        if (titles.length >= 2) {
            holder.chapterTitleView.setText(titles[1]);
            holder.chapterNumberView.setText(titles[0]);
        } else {
            holder.chapterTitleView.setText(post.getTitle());
            holder.chapterNumberView.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
