
package com.dawhey.mlij_blogapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    private boolean isNew;

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

    public Calendar getPublishedDate() throws ParseException {
        Locale locale = new Locale("pl", "PL");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", locale);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(df.parse(published));
        return calendar;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
