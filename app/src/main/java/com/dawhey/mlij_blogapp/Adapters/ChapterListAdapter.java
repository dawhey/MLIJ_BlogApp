package com.dawhey.mlij_blogapp.Adapters;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.R;
import java.util.List;

/**
 * Created by dawhey on 17.03.17.
 */
public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder> {



    private List<Chapter> posts;
    private OnChapterClickListener listener;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chapterTitleView, chapterNumberView;
        public ImageView favoriteIconView;

        ViewHolder(View v) {
            super(v);
            chapterTitleView = (TextView) v.findViewById(R.id.chapter_title_view);
            chapterNumberView = (TextView) v.findViewById(R.id.chapter_number_view);
            favoriteIconView = (ImageView) v.findViewById(R.id.chapter_favourites_icon);
        }
    }

    public void setOnChapterClickListener(OnChapterClickListener fragment) {
        listener = fragment;
    }

    public void setPosts(List<Chapter> posts) {
        this.posts = posts;
    }

    @Override
    public ChapterListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Chapter post = posts.get(position);
        holder.chapterTitleView.setText(post.getTitleFormatted());
        holder.chapterNumberView.setText(post.getChapterHeaderFormatted());

        ViewCompat.setTransitionName(holder.chapterTitleView, String.valueOf(position) + "_chapterTitle");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChapterItemClick(posts.get(position), holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface OnChapterClickListener {
        void onChapterItemClick(Chapter chapter, ViewHolder holder);
    }
}
