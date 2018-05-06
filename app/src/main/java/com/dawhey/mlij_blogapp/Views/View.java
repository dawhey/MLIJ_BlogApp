package com.dawhey.mlij_blogapp.Views;

public interface View<T> {

    void showContent(T content);

    void showError();

    void showLoading();
}
