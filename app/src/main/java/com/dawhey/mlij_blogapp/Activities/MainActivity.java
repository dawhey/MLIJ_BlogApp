package com.dawhey.mlij_blogapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dawhey.mlij_blogapp.Fragments.AboutBlogFragment;
import com.dawhey.mlij_blogapp.Fragments.ChaptersListFragment;
import com.dawhey.mlij_blogapp.Fragments.FavoritesFragment;
import com.dawhey.mlij_blogapp.R;
import com.google.firebase.messaging.FirebaseMessaging;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TOPIC = "news";
    public static final String TAG_FRAGMENT_TO_RETAIN = "RetainFragment";
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeDrawerMenu();
        retainFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void retainFragment() {
        Fragment retainedFragment = getRetainedFragment();
        if (retainedFragment != null) {
            openFragment(retainedFragment);
        } else {
            openFragment(new ChaptersListFragment());
            navigationView.setCheckedItem(R.id.nav_chapters);
        }
    }

    private void initializeDrawerMenu() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        item.setChecked(true);
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_chapters) {
            fragment = new ChaptersListFragment();
        } else if (id == R.id.nav_favourites) {
            fragment = new FavoritesFragment();
        } else if (id == R.id.nav_about_blog) {
            fragment = new AboutBlogFragment();
        }

        new Handler().postDelayed(new OpenFragmentRunnable(fragment), OpenFragmentRunnable.DELAY);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment getRetainedFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentByTag(TAG_FRAGMENT_TO_RETAIN);
    }

    private void openFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment, TAG_FRAGMENT_TO_RETAIN);
            ft.commit();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Class used to avoid Navigation Drawer closing stutter
     * when new Fragment is selected
     */
    private class OpenFragmentRunnable implements Runnable {

        private static final int DELAY = 300;
        private Fragment fragment;

        OpenFragmentRunnable(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void run() {
            openFragment(fragment);
        }
    }

}
