package com.dawhey.mlij_blogapp.Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Activities.MainActivity;
import com.dawhey.mlij_blogapp.Listeners.OnChangeFontSizeListener;
import com.dawhey.mlij_blogapp.Listeners.OnChapterDownloadedListener;
import com.dawhey.mlij_blogapp.Listeners.OnChapterScrollListener;
import com.dawhey.mlij_blogapp.Managers.PreferencesManager;
import com.dawhey.mlij_blogapp.Models.Bookmark;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Presenters.ChapterFragmentPresenter;
import com.dawhey.mlij_blogapp.R;
import com.dawhey.mlij_blogapp.Repositories.ChaptersRepositoryImpl;
import com.dawhey.mlij_blogapp.Utils.FirebaseLogger;
import com.dawhey.mlij_blogapp.Views.ChapterView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.dawhey.mlij_blogapp.Listeners.OnChapterScrollListener.CONTENT_BEGINNING;

/**
 * Created by dawhey on 18.03.17.
 */

public class ChapterFragment extends Fragment implements
       ChapterView {

    public static final int SLIDER_FONT_OFFSET = 15;

    private static final String FIRST_VISIBLE_CHAR_KEY = "firstVisibleChar";
    private int fontSize = 0;
    private int changedFontSize = 0;
    private int firstVisibleCharacterOffset;

    private boolean openedFromBookmark;
    private Chapter chapter;

    private PreferencesManager preferencesManager;
    private ChapterFragmentPresenter presenter;
    private OnChapterDownloadedListener listener;

    @BindView(R.id.font_slider)
    SeekBar fontSlider;

    @BindView(R.id.accept_font_size)
    ImageView changeFontButtonDone;

    @BindView(R.id.error_chapter_view)
    RelativeLayout errorView;

    @BindView(R.id.font_slider_view)
    RelativeLayout changeFontSizeView;

    @BindView(R.id.text_scroll_view)
    ScrollView scrollView;

    @BindView(R.id.error_refresh_button)
    FloatingActionButton refreshButton;

    @BindView(R.id.chapter_progressbar)
    ProgressBar progressBar;

    @BindView(R.id.chapter_content_view)
    TextView contentView;

    @BindView(R.id.display_font_size_text)
    TextView displayFontSizeView;

    @BindView(R.id.chapter_title_view)
    TextView titleView;

    @BindView(R.id.title_divider)
    View dividerLine;

    @BindView(R.id.favorite_button)
    FloatingActionButton favoriteButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = PreferencesManager.getInstance(getContext());
        chapter = preferencesManager.getLastChapter();
        fontSize = preferencesManager.getChapterFontSize();
        changedFontSize = fontSize;
        presenter = new ChapterFragmentPresenter(new ChaptersRepositoryImpl(getContext()), this, AndroidSchedulers.mainThread(), chapter.getId(), listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chapter, container, false);
        ButterKnife.bind(this, root);
        setHasOptionsMenu(true);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle(chapter.getChapterHeaderFormatted());
        initViews();
        initViewListeners();
        return root;
    }

    private void initViews() {
        dividerLine.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        titleView.setText(chapter.getTitleFormatted());
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        displayFontSizeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        fontSlider.setProgress(fontSize - SLIDER_FONT_OFFSET);

        //Setting up favorite button
        if (preferencesManager.isInFavorites(chapter)) {
            favoriteButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        } else {
            favoriteButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textDefault)));
        }
    }

    private void initViewListeners() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnChapterScrollListener(favoriteButton, scrollView));
        fontSlider.setOnSeekBarChangeListener(new OnChangeFontSizeListener(this, displayFontSizeView));
    }

    @OnClick(R.id.accept_font_size)
    public void  onChangeFontButtonClick() {
        if (changedFontSize != fontSize) {
            contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, changedFontSize);
            preferencesManager.setChapterFontSize(changedFontSize);
            Snackbar.make(favoriteButton, R.string.default_fontsize_changed, Snackbar.LENGTH_LONG).show();
            fontSize = changedFontSize;
        }
        hideFontSliderView();
    }

    @OnClick(R.id.error_refresh_button)
    public void onRefreshButtonClick() {
        presenter.loadContent();
    }

    @OnClick(R.id.favorite_button)
    public void onFavoriteButtonClick() {
        favoriteButton.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up));
        if (preferencesManager.isInFavorites(chapter)) {
            preferencesManager.removeFromFavorites(chapter);
            favoriteButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textDefault)));
            Snackbar.make(favoriteButton, R.string.deleted_from_favorites_message, Snackbar.LENGTH_SHORT).show();
        } else {
            preferencesManager.addToFavorites(chapter);
            favoriteButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            Snackbar.make(favoriteButton, R.string.added_to_favorites_message, Snackbar.LENGTH_SHORT).show();
            FirebaseLogger.getInstance(getContext()).logAddingToFavoritesEvent(chapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        chapter = preferencesManager.getLastChapter();
        if (chapter.getContent() == null) {
            presenter.loadContent();
        } else {
            showContent(chapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    /**
     * Sets listener which gets notified if Chapter was downloaded
     * so the listener can cache it
     * @param listener to notify
     */
    public void setOnChapterDownloadedListener(OnChapterDownloadedListener listener) {
        this.listener = listener;
    }

    public void setOpenedFromBookmark(boolean openedFromBookmark) {
        this.openedFromBookmark = openedFromBookmark;
    }

    /**
     * Checks if scroll offset variable was saved
     * @param savedInstanceState bundle
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            firstVisibleCharacterOffset = savedInstanceState.getInt(FIRST_VISIBLE_CHAR_KEY);
        }
    }

    private void scrollToPixelOffset(final int firstVisibleCharacterOffset) {
        scrollView.post(new Runnable() {
            public void run() {
                final int firstVisibleLineOffset = contentView.getLayout().getLineForOffset(firstVisibleCharacterOffset);
                final int pixelOffset = contentView.getLayout().getLineTop(firstVisibleLineOffset);
                scrollView.scrollTo(CONTENT_BEGINNING, pixelOffset);
            }
        });
    }

    /**
     * scroll offset is saved
     * @param outState containing scroll offset
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (contentView.getLayout() != null) {
            int firstVisibleCharacterOffset = getFirstVisibleCharacterOffset();
            outState.putInt(FIRST_VISIBLE_CHAR_KEY, firstVisibleCharacterOffset);
        }
    }

    /**
     * get scroll offset for first line visible, so in orientation change
     * user can continue reading where he left
     * @return scroll offset
     */
    private int getFirstVisibleCharacterOffset() {
        final int firstVisibleLineOffset = contentView.getLayout().getLineForVertical(scrollView.getScrollY());
        return contentView.getLayout().getLineStart(firstVisibleLineOffset);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chapter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Add bookmark to chapter or
     * Show view where you can change default font size
     * @param item which was pressed
     * @return false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_last_read) {
            Bookmark bookmark = new Bookmark(getFirstVisibleCharacterOffset(), chapter);
            preferencesManager.saveBookmark(bookmark);
            Snackbar.make(favoriteButton, R.string.saved_bookmark, Snackbar.LENGTH_SHORT).show();
            FirebaseLogger.getInstance(getContext()).logPlaceBookmark(chapter);
        } else if (item.getItemId() == R.id.action_change_fontsize && changeFontSizeView.getVisibility() != View.VISIBLE) {
            showFontSliderView();
        }
        return false;
    }

    public RelativeLayout getChangeFontSizeView() {
        return changeFontSizeView;
    }

    public void setChangedFontSize(int fontSize) {
        this.changedFontSize = fontSize;
    }

    @Override
    public void showContent(Chapter content) {
        preferencesManager.setLastChapter(content);
        contentView.setText(Html.fromHtml(content.getContent()));
        if (firstVisibleCharacterOffset != 0) {
            scrollToPixelOffset(firstVisibleCharacterOffset);
        }
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        favoriteButton.show();
        animateContent();

        if (openedFromBookmark) {
            Bookmark bookmark = preferencesManager.getBookmark();
            scrollToPixelOffset(bookmark.getPosition());
            Snackbar.make(favoriteButton, R.string.opened_from_bookmark, Snackbar.LENGTH_SHORT).show();
            openedFromBookmark = false;
        }
    }

    private void animateContent() {
        if (getContext() != null) {
            Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            a.reset();
            contentView.clearAnimation();
            contentView.startAnimation(a);
            favoriteButton.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up));
        }
    }

    @Override
    public void showError() {
        progressBar.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up_faster));
    }

    @Override
    public void showLoading() {
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_faster));
    }

    private void showFontSliderView() {
        changeFontSizeView.setVisibility(View.VISIBLE);
        changeFontSizeView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up_faster));
    }

    public void hideFontSliderView() {
        changeFontSizeView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_down_faster));
        changeFontSizeView.setVisibility(View.GONE);
    }
}