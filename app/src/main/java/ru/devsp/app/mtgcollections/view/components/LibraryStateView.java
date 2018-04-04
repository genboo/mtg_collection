package ru.devsp.app.mtgcollections.view.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.devsp.app.mtgcollections.R;
import ru.devsp.app.mtgcollections.model.objects.CardColorState;
import ru.devsp.app.mtgcollections.model.objects.CardManaState;

/**
 * Статистика колоды
 * Created by gen on 06.10.2017.
 */

public class LibraryStateView extends View {

    private static final int TEXT_SIZE = 22;
    private Paint mTextPaint;
    private Paint mColorPaint;

    private List<CardManaState> mManaState;
    private List<CardColorState> mColorState;
    private float mMargin;
    private float mColWidth;
    private float mColorGraphSize;
    private float mColorGraphStrokeWidth;
    private int mMainColor;
    private int mColorCreatuers;

    public LibraryStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LibraryStateView,
                0, 0);

        mMainColor = a.getColor(R.styleable.LibraryStateView_color, getColor(R.color.colorPrimary));
        mColorCreatuers = a.getColor(R.styleable.LibraryStateView_colorCreatures, getColor(R.color.colorAccent));
        mMargin = a.getDimension(R.styleable.LibraryStateView_manaGraphMargin, 0);
        mColWidth = a.getDimension(R.styleable.LibraryStateView_manaColWidth, 0);
        mColorGraphSize = a.getDimension(R.styleable.LibraryStateView_colorGraphSize, 45f);
        mColorGraphStrokeWidth = a.getDimension(R.styleable.LibraryStateView_colorGraphStrokeWidth, 7f);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mMainColor);
        mTextPaint.setTextSize(TEXT_SIZE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorPaint.setStyle(Paint.Style.STROKE);

        if (isInEditMode()) {
            mManaState = new ArrayList<>();
            for (int i = 1; i < 7; i++) {
                CardManaState state = new CardManaState();
                state.cmc = i;
                state.count = i;
                mManaState.add(state);
            }

            mColorState = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                CardColorState state = new CardColorState();
                state.color = getEditColor(i);
                state.count = 10;
                mColorState.add(state);
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mManaState != null && !mManaState.isEmpty()) {
            drawManaState(canvas);
        }
        if (mColorState != null && !mColorState.isEmpty()) {
            drawColorState(canvas);
        }
    }

    /**
     * Отрисовка распределения по цветам
     *
     * @param canvas Канвас
     */
    private void drawColorState(Canvas canvas) {
        int full = getFullSize();
        float angle = 0;

        mColorPaint.setColor(mMainColor);
        mColorPaint.setStrokeWidth(mColorGraphStrokeWidth * 1.3f);
        canvas.drawCircle(getWidth() - getPaddingRight() - mColorGraphSize - mColorGraphStrokeWidth,
                (getHeight() / 2f) + getPaddingBottom(), mColorGraphSize, mColorPaint);

        mColorPaint.setStrokeWidth(mColorGraphStrokeWidth);
        for (CardColorState state : mColorState) {
            float percent = state.count / (float) full;
            mColorPaint.setColor(getStateColor(state));
            RectF rect = new RectF(
                    getWidth() - getPaddingRight() - 2 * mColorGraphSize - mColorGraphStrokeWidth,
                    getHeight() / 2f - mColorGraphSize + getPaddingBottom(),
                    getWidth() - getPaddingRight() - mColorGraphStrokeWidth,
                    getHeight() / 2f + mColorGraphSize + getPaddingBottom());
            canvas.drawArc(rect, angle, percent * 360f, false, mColorPaint);
            mColorPaint.setColor(mMainColor);
            angle += percent * 360f;
            canvas.drawArc(rect, angle - 2f, 2f, false, mColorPaint);
        }

    }

    /**
     * Отрисовка кривой маны
     *
     * @param canvas Канвас
     */
    private void drawManaState(Canvas canvas) {
        int max = getMaximum();
        mTextPaint.setColor(mMainColor);
        for (int i = 0; i < mManaState.size(); i++) {
            CardManaState state = mManaState.get(i);
            canvas.drawText(Integer.toString(state.cmc),
                    getPaddingLeft() + i * mColWidth + mColWidth / 2 - mMargin / 2 + mMargin,
                    getHeight() - (float) getPaddingBottom(),
                    mTextPaint);

            float percent = state.count / (float) max;
            float height = (getHeight() - getPaddingBottom() - getPaddingTop()) - (getHeight() - getPaddingBottom() - getPaddingTop()) * percent;
            Rect rect = new Rect(
                    getPaddingLeft() + i * Math.round(mColWidth) + Math.round(mMargin),
                    getPaddingTop() + Math.round(height),
                    getPaddingLeft() + i * Math.round(mColWidth) + Math.round(mColWidth),
                    getHeight() - getPaddingBottom() - TEXT_SIZE);
            canvas.drawRect(rect, mTextPaint);
        }
        mTextPaint.setColor(mColorCreatuers);
        for (int i = 0; i < mManaState.size(); i++) {
            CardManaState state = mManaState.get(i);
            if(state.creatures > 0) {
                float percent = state.creatures / (float) max;
                float height = (getHeight() - getPaddingBottom() - getPaddingTop()) - (getHeight() - getPaddingBottom() - getPaddingTop()) * percent;
                Rect rect = new Rect(
                        getPaddingLeft() + i * Math.round(mColWidth) + Math.round(mMargin),
                        getPaddingTop() + Math.round(height),
                        getPaddingLeft() + i * Math.round(mColWidth) + Math.round(mColWidth),
                        getHeight() - getPaddingBottom() - TEXT_SIZE);
                canvas.drawRect(rect, mTextPaint);
            }
        }
    }

    private int getFullSize() {
        int full = 0;
        for (CardColorState state : mColorState) {
            full += state.count;
        }
        if (full == 0) {
            return 1;
        }
        return full;
    }

    private int getStateColor(CardColorState state) {
        if (state.color == null) {
            return getColor(R.color.colorNoColor);
        }
        switch (state.color) {
            case "Red":
                return getColor(R.color.colorRed);
            case "Green":
                return getColor(R.color.colorGreen);
            case "Blue":
                return getColor(R.color.colorBlue);
            case "Black":
                return getColor(R.color.colorBlack);
            default:
                return getColor(R.color.colorWhite);
        }
    }

    private int getMaximum() {
        int max = 1;
        for (CardManaState state : mManaState) {
            if (state.count > max) {
                max = state.count;
            }
        }
        return max;
    }

    public void setManaState(List<CardManaState> states) {
        mManaState = states;
        invalidate();
    }

    public void setColorState(List<CardColorState> states) {
        mColorState = states;
        invalidate();
    }

    private String getEditColor(int color) {
        switch (color) {
            case 0:
                return "Red";
            case 1:
                return "Blue";
            case 2:
                return "Green";
            case 3:
                return "Black";
            case 4:
                return "White";
            default:
                return null;
        }
    }

    private int getColor(int color) {
        return ContextCompat.getColor(getContext(), color);
    }

}
