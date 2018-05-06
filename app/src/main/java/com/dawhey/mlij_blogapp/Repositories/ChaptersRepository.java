package com.dawhey.mlij_blogapp.Repositories;

import com.dawhey.mlij_blogapp.Models.Bookmark;
import com.dawhey.mlij_blogapp.Models.Chapter;
import com.dawhey.mlij_blogapp.Models.Posts;

import java.util.List;

import io.reactivex.Single;

public interface ChaptersRepository {

    Single<Posts> getAllChapters();

    void saveOldChapters(Posts posts);

    Single<Chapter> getChapter(String id);

    boolean isInFavorites(Chapter chapter);

    void addToFavorites(Chapter chapter);

    void removeFromFavorites(Chapter chapter);

    void saveBookmark(Bookmark bookmark);

    void setChapterFontSize(int changedFontSize);

    void setLastChapter(Chapter chapter);

    Bookmark getBookmark();

    Chapter getLastChapter();
}
