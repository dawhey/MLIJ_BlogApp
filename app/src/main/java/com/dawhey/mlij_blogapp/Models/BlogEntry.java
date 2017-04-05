package com.dawhey.mlij_blogapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dawhe on 05.04.2017.
 */

public class BlogEntry {

    private static final String SDF_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    @SerializedName("id")
    @Expose
    protected String id;

    @SerializedName("content")
    @Expose
    protected String content;

    @SerializedName("published")
    @Expose
    protected String published;

    @SerializedName("url")
    @Expose
    protected String url;

    @SerializedName("title")
    @Expose
    protected String title;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getPublishedDate() throws ParseException {
        Locale locale = new Locale("pl", "PL");
        SimpleDateFormat df = new SimpleDateFormat(SDF_PATTERN, locale);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(df.parse(published));
        return calendar;
    }
}
