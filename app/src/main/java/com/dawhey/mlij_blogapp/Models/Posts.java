
package com.dawhey.mlij_blogapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Posts {

    @SerializedName("items")
    @Expose
    private List<Chapter> chapters;

    public Posts(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

}
