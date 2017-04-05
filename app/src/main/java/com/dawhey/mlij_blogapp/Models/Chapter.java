package com.dawhey.mlij_blogapp.Models;

public class Chapter extends BlogEntry {

    public String getTitleFormatted() {
        String[] titles = title.split(": ");
        if (titles.length >= 2) {
            return titles[1];
        } else {
            return title;
        }
    }

    public String getChapterHeaderFormatted() {
        String[] titles = title.split(": ");
        if (titles.length >= 2) {
            return titles[0];
        } else {
            return title;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Chapter) {
            return this.id.equals(((Chapter) obj).id);
        } else {
            return false;
        }
    }
}
