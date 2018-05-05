package com.dawhey.mlij_blogapp.Listeners;

import android.util.TypedValue;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dawhey.mlij_blogapp.Fragments.ChapterFragment;

import static com.dawhey.mlij_blogapp.Fragments.ChapterFragment.SLIDER_FONT_OFFSET;

public class OnChangeFontSizeListener implements SeekBar.OnSeekBarChangeListener {

    private ChapterFragment fragment;
    private TextView displayFontSizeView;

    public OnChangeFontSizeListener(ChapterFragment fragment, TextView displayFontSizeView) {
        this.fragment = fragment;
        this.displayFontSizeView = displayFontSizeView;
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
}
