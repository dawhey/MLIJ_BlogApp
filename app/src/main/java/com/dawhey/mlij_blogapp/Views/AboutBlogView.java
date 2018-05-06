package com.dawhey.mlij_blogapp.Views;

import android.content.Intent;

public interface AboutBlogView {

    void startActivity(Intent intent);

    void showSnackbar(int message, int length);
}
