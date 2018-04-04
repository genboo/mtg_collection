package ru.devsp.app.mtgcollections.view.components;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Трансформации для галерии
 * Created by gen on 09.10.2017.
 */

public class GalleryPageTransform implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(View view, float position) {
        if (position <= 0f) {
            view.setTranslationX(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);
        } else if (position <= 1f) {
            final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setAlpha(1 - position);
            view.setPivotY(0.5f * view.getHeight());
            view.setTranslationX(view.getWidth() * -position);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
    }

}
