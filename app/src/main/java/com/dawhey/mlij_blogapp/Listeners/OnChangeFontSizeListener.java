package com.dawhey.mlij_blogapp.Listeners;

import android.util.TypedValue;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Fragments.ChapterFragment;

import static com.dawhey.mlij_blogapp.Fragments.ChapterFragment.SLIDER_FONT_OFFSET;

public class OnChangeFontSizeListener implements SeekBar.OnSeekBarChangeListener {

    private ChapterFragment fragment;
    private TextView displayFontSizeView;

    private int fontSize;
    private int changedFontSize;

    public OnChangeFontSizeListener(ChapterFragment fragment, TextView displayFontSizeView, int fontSize) {
        this.fragment = fragment;
        this.displayFontSizeView = displayFontSizeView;
        this.fontSize = fontSize;
        changedFontSize = fontSize;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int fontSize = SLIDER_FONT_OFFSET + i;
        fragment.setChangedFontSize(fontSize);
        displayFontSizeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public int getChangedFontSize() {
        return changedFontSize;
    }

    public void setChangedFontSize(int changedFontSize) {
        this.changedFontSize = changedFontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
