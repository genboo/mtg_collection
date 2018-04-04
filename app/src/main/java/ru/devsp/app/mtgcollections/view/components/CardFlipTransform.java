package ru.devsp.app.mtgcollections.view.components;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Трансформации для карт
 * Created by gen on 09.10.2017.
 */

public class CardFlipTransform implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        float percentage = 1 - Math.abs(position);
        page.setCameraDistance(12000);
        setVisibility(page, position);
        setTranslation(page);
        setRotation(page, position, percentage);
    }

    private void setVisibility(View page, float position) {
        if (position < 0.5 && position > -0.5) {
            page.setVisibility(View.VISIBLE);
        } else {
            page.setVisibility(View.INVISIBLE);
        }
    }

    private void setTranslation(View page) {
        ViewPager viewPager = (ViewPager) page.getParent();
        int scroll = viewPager.getScrollX() - page.getLeft();
        page.setTranslationX(scroll);
    }

    private void setRotation(View page, float position, float percentage) {
        if (position > 0) {
            page.setRotationY(-180 * (percentage + 1));
        } else {
            page.setRotationY(180 * (percentage + 1));
        }
    }

}
