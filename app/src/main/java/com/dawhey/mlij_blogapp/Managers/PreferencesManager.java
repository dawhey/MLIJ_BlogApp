package com.dawhey.mlij_blogapp.Managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.dawhey.mlij_blogapp.Models.Chapter;
import com.google.gson.Gson;

public class PreferencesManager {

    private static final String PREF_NAME = "prefs";
    private static final String LAST_CHAPTER_KEY = "LastChapter";

    private static PreferencesManager instance;
    private final SharedPreferences preferences;

    private PreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public static synchronized PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    public void setLastChapter(Chapter chapter) {
        String json = new Gson().toJson(chapter);
        preferences.edit().putString(LAST_CHAPTER_KEY, json).commit();
    }

    public Chapter getLastChapter() {
        String json = preferences.getString(LAST_CHAPTER_KEY, null);
        if (json != null) {
            return new Gson().fromJson(json, Chapter.class);
        } else {
            return null;
        }
    }
}