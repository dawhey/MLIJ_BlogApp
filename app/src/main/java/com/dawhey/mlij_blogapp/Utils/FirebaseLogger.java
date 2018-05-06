package com.dawhey.mlij_blogapp.Utils;

import android.content.Context;
import android.os.Bundle;

import com.dawhey.mlij_blogapp.Models.Chapter;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseLogger {

    private static final String CONTENT_TYPE_CHAPTER = "chapter";
    private static final String EVENT_FAVORITE = "Favorite";
    private static final String EVENT_PLACE_BOOKMARK = "Bookmark";

    private static FirebaseLogger singleton;
    private Context context;

    private FirebaseLogger(Context context) {
        this.context = context;
    }

    public static FirebaseLogger getInstance(Context context) {
        if (singleton == null) {
            singleton = new FirebaseLogger(context);
        }
        return singleton;
    }

    public void logAddingToFavoritesEvent(Chapter chapter) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_FAVORITE, setupBundle(chapter));
    }

    public void logPlaceBookmark(Chapter chapter) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_PLACE_BOOKMARK, setupBundle(chapter));
    }

    private Bundle setupBundle(Chapter chapter) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, chapter.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, chapter.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CONTENT_TYPE_CHAPTER);
        return bundle;
    }
}
