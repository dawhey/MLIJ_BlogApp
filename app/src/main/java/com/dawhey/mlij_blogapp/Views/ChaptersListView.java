package com.dawhey.mlij_blogapp.Views;

import com.dawhey.mlij_blogapp.Models.Posts;

public interface ChaptersListView extends View<Posts> {

    void showNoChapters();

    void updateChapters(Posts posts);
}
