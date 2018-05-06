package com.dawhey.mlij_blogapp.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Filters.ChaptersTitleFilter;
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
public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListViewHolder> implements Filterable {

    private List<Chapter> posts;
    private Context context;
    private Fragment callingFragment;
    private List<Chapter> favorites;
    private PreferencesManager preferencesManager;

    private String queryText;
    private ChaptersTitleFilter filter;

    private ColorStateList regularTint;
    private ColorStateList favoriteTint;



    public ChapterListAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.callingFragment = fragment;
        preferencesManager = PreferencesManager.getInstance(context);
        favorites = preferencesManager.getFavoriteChapters();

        regularTint = ColorStateList.valueOf(context.getResources().getColor(R.color.grayBright));
        favoriteTint = ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent));
    }

    public void setPosts(List<Chapter> posts) {
        this.posts = posts;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ChaptersTitleFilter(this, posts);
        }
        return filter;
    }

    @Override
    public ChapterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ChapterListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ChapterListViewHolder holder, final int position) {
        Chapter post = posts.get(position);
        handleSearchQuery(holder, post);
        holder.chapterNumberView.setText(post.getChapterHeaderFormatted());
        holder.favoriteIconView.setImageTintList(favorites.contains(post) ? favoriteTint : regularTint);
        holder.chapterNewView.setVisibility(!preferencesManager.isInOldChapters(post.getId()) ? View.VISIBLE : View.INVISIBLE);

        ViewCompat.setTransitionName(holder.chapterTitleView, String.valueOf(position) + "_chapterTitle");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(holder);
            }
        });
    }

    private void handleSearchQuery(ChapterListViewHolder holder, Chapter post) {
        if (queryText != null && !queryText.isEmpty()) {
            holder.chapterTitleView.setText(getSpannableFromQuery(queryText, post.getTitleFormatted()));
        } else {
            holder.chapterTitleView.setText(post.getTitleFormatted());
        }
    }

    private void handleClick(ChapterListViewHolder holder) {
        Chapter clicked = posts.get(holder.getAdapterPosition());
        launchChapterFragment(clicked, holder);

    }

    /**
     * Highlighting text entered to search view
     * @param queryText text entered to searchview
     * @param originalText chapter title
     * @return highlighted text if possible
     */
    private CharSequence getSpannableFromQuery(String queryText, String originalText) {
        int startPos = originalText.toLowerCase().indexOf(queryText.toLowerCase());
        int endPos = startPos + queryText.length();

        if (startPos != -1) {
            Spannable spannable = new SpannableString(originalText);
            ColorStateList highlightColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.primaryBlue)});
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, highlightColor, null);

            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannable;
        } else {
            return originalText;
        }
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void launchChapterFragment(Chapter chapter, ChapterListViewHolder holder) {
        PreferencesManager manager = PreferencesManager.getInstance(context);
        manager.setLastChapter(chapter);
        if (!preferencesManager.isInOldChapters(chapter.getId())) {
            manager.addToOldChapters(chapter.getId());
        }
        ChapterFragment chapterFragment = new ChapterFragment();

        if (queryText == null || queryText.isEmpty()) {
            chapterFragment.setSharedElementEnterTransition(new DetailsTransition());
            chapterFragment.setSharedElementReturnTransition(new DetailsTransition());
        }
        chapterFragment.setEnterTransition(new Fade());
        callingFragment.setExitTransition(new Fade());

        if (callingFragment instanceof ChaptersListFragment) {
            chapterFragment.setOnChapterDownloadedListener((ChaptersListFragment) callingFragment);
        }

        ((MainActivity) callingFragment.getActivity()).setCurrentVisibleFragment(ChapterFragment.class);
        callingFragment.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.chapterTitleView, context.getString(R.string.shared_transition_title_tag))
                .replace(R.id.content_main, chapterFragment, MainActivity.TAG_FRAGMENT_TO_RETAIN)
                .addToBackStack(null)
                .commit();
    }
}
