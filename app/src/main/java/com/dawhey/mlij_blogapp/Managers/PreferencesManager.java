package com.dawhey.mlij_blogapp.Managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.dawhey.mlij_blogapp.Models.Chapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private static final String PREF_NAME = "prefs";
    private static final String LAST_CHAPTER_KEY = "LastChapter";
    private static final String FAVORITES_KEY = "FavoriteChapters";


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

    public List<Chapter> getFavoriteChapters() {
        String json = preferences.getString(FAVORITES_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Chapter>>(){}.getType();
            return gson.fromJson(json, listType);
        } else {
            return new ArrayList<>();
        }
    }

    private void saveFavoriteChapters(List<Chapter> favorites) {
        String json = new Gson().toJson(favorites);
        preferences.edit().putString(FAVORITES_KEY, json).commit();
    }

    public boolean isInFavorites(Chapter chapter) {
        return getFavoriteChapters().contains(chapter);
    }

    /**
     *
     * @param chapter to be added;
     * @return true if chapter was added,
     * false if this chapter was already in the list
     */
    public boolean addToFavorites(Chapter chapter) {
        List<Chapter> favorites = getFavoriteChapters();
        if (!favorites.contains(chapter)) {
            favorites.add(chapter);
            saveFavoriteChapters(favorites);
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param chapter to be removed
     * @return true if chapterId was removed succesfully,
     * false if there was no such chapter in list
     */
    public boolean removeFromFavorites(Chapter chapter) {
        List<Chapter> favorites = getFavoriteChapters();
        if (favorites.contains(chapter)) {
            favorites.remove(chapter);
            saveFavoriteChapters(favorites);
            return true;
        } else {
            return false;
        }
    }
}