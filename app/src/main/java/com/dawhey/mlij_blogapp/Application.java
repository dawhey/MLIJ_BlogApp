package com.dawhey.mlij_blogapp;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by dawhey on 19.03.17.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/alegreya.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
