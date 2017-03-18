package com.dawhey.mlij_blogapp.Transitions;

import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

/**
 * Created by dawhey on 18.03.17.
 */
public class DetailsTransition extends TransitionSet {
    public DetailsTransition() {
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).addTransition(new ChangeTransform());
        setDuration(400);
    }
}