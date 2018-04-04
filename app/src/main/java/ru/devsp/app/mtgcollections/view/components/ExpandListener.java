package ru.devsp.app.mtgcollections.view.components;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * Анимация стрелок
 * Created by gen on 28.12.2017.
 */

public class ExpandListener implements ExpandableTextView.OnExpandListener {

    private View arrow;

    public ExpandListener(View arrow) {
        this.arrow = arrow;
    }

    @Override
    public void onExpand(@NonNull ExpandableTextView view) {
        createRotateAnimator(arrow, 0f, 180f).start();
    }

    @Override
    public void onCollapse(@NonNull ExpandableTextView view) {
        createRotateAnimator(arrow, 180f, 0f).start();
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(200);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}
