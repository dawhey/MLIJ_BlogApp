package com.dawhey.mlij_blogapp;

import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by dawhey on 19.03.17.
 */

public class Application extends android.app.Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/alegreya.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Context getAppContext() {
        return Application.context;
    }
}
