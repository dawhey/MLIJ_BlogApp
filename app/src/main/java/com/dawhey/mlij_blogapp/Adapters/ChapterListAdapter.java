package com.dawhey.mlij_blogapp.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Fragments.ChapterFragment;
import com.dawhey.mlij_blogapp.Fragments.ChaptersListFragment;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.R;
import com.dawhey.mlij_blogapp.Transitions.DetailsTransition;

import java.util.List;

/**
 * Created by dawhey on 17.03.17.
 */
public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder> {

    private List<Chapter> posts;
    private OnChapterClickListener listener;
    private Context context;
    private Fragment callingFragment;
    private List<Chapter> favorites;
    private List<String> oldChapters;

    private ColorStateList regularTint;
    private ColorStateList favoriteTint;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTitleView, chapterNumberView, chapterNewView;
        ImageView favoriteIconView;

        ViewHolder(View v) {
            super(v);
            chapterTitleView = (TextView) v.findViewById(R.id.chapter_title_view);
            chapterNumberView = (TextView) v.findViewById(R.id.chapter_number_view);
            favoriteIconView = (ImageView) v.findViewById(R.id.chapter_favourites_icon);
            chapterNewView = (TextView) v.findViewById(R.id.chapter_new_label);
        }
    }

    public ChapterListAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.callingFragment = fragment;
        PreferencesManager preferencesManager = PreferencesManager.getInstance(context);
        favorites = preferencesManager.getFavoriteChapters();
        oldChapters = preferencesManager.getOldChapters();

        regularTint = ColorStateList.valueOf(context.getResources().getColor(R.color.grayBright));
        favoriteTint = ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent));
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
        holder.favoriteIconView.setImageTintList(favorites.contains(post) ? favoriteTint : regularTint);
        holder.chapterNewView.setVisibility(!oldChapters.contains(post.getId()) ? View.VISIBLE : View.INVISIBLE);

        ViewCompat.setTransitionName(holder.chapterTitleView, String.valueOf(position) + "_chapterTitle");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(holder);
            }
        });

    }

    private void handleClick(ViewHolder holder) {
        Chapter clicked = posts.get(holder.getAdapterPosition());
        if (listener != null) {
            listener.onChapterItemClick(clicked, holder);
        } else {
            launchChapterFragment(clicked, holder);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void launchChapterFragment(Chapter chapter, ViewHolder holder) {
        PreferencesManager manager = PreferencesManager.getInstance(context);
        manager.setLastChapter(chapter);
        if (!oldChapters.contains(chapter.getId())) {
            manager.addToOldChapters(chapter.getId());
        }
        ChapterFragment chapterFragment = new ChapterFragment();
        chapterFragment.setSharedElementEnterTransition(new DetailsTransition());
        chapterFragment.setSharedElementReturnTransition(new DetailsTransition());
        chapterFragment.setEnterTransition(new Fade());
        callingFragment.setExitTransition(new Fade());

        if (callingFragment instanceof ChaptersListFragment) {
            chapterFragment.setOnChapterDownloadedListener((ChaptersListFragment) callingFragment);
        }

        callingFragment.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.chapterTitleView, context.getString(R.string.shared_transition_title_tag))
                .replace(R.id.content_main, chapterFragment, MainActivity.TAG_FRAGMENT_TO_RETAIN)
                .addToBackStack(null)
                .commit();
    }
}
