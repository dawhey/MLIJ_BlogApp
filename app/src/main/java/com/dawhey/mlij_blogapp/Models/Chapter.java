
package com.dawhey.mlij_blogapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chapter {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("published")
    @Expose
    private String published;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormattedPublishDate() {
        return published.split("T")[0];
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
