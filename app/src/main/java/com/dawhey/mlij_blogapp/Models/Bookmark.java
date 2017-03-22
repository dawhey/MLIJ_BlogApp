package com.dawhey.mlij_blogapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dawhey on 22.03.17.
 */

public class Bookmark {

    @SerializedName("position")
    @Expose
    private int position;

    @SerializedName("chapter")
    @Expose
    private Chapter chapter;

    public Bookmark(int position, Chapter chapter) {
        this.position = position;
        this.chapter = chapter;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}

