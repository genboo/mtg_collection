package ru.devsp.app.mtgcollections.view.components;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import ru.devsp.app.mtgcollections.R;

/**
 * Спойлер
 * Created by gen on 13.12.2017.
 */

public class ExpandableTextView extends AppCompatTextView {

    private static final int DEFAULT_ANIMATION_SPEED = 100;

    private int mAnimationSpeed;
    private int mMaxLines;
    private boolean mAnimating;
    private boolean mExpanded;
    private int mCollapsedHeight;

    private TimeInterpolator mAnimationInterpolator;

    private OnExpandListener mListener;

    private SavedState mSavedState;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, defStyle, 0);
        mAnimationSpeed = attributes.getInt(R.styleable.ExpandableTextView_animation_speed, DEFAULT_ANIMATION_SPEED);
        attributes.recycle();

        mMaxLines = getMaxLines();

        mAnimationInterpolator = new AccelerateDecelerateInterpolator();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, int heightMeasureSpec) {
        if (mSavedState != null) {
            mExpanded = mSavedState.getExpanded();
            if(mExpanded){
                setMaxHeight(Integer.MAX_VALUE);
                setMaxLines(Integer.MAX_VALUE);
            }
            mSavedState = null;
        }
        if (mMaxLines == 0 && !mExpanded && !mAnimating) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean toggle() {
        return mExpanded ? collapse() : expand();
    }

    public boolean expand() {
        if (!mExpanded && !mAnimating && mMaxLines >= 0) {
            notifyOnExpand();
            measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            mCollapsedHeight = getMeasuredHeight();
            mAnimating = true;

            setMaxLines(Integer.MAX_VALUE);

            measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            final int expandedHeight = getMeasuredHeight();

            final ValueAnimator valueAnimator = ValueAnimator.ofInt(mCollapsedHeight, expandedHeight);
            valueAnimator.addUpdateListener(animation ->
                    setHeight((int) animation.getAnimatedValue()));

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    setMaxHeight(Integer.MAX_VALUE);
                    setMinHeight(0);

                    final ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    setLayoutParams(layoutParams);

                    mExpanded = true;
                    mAnimating = false;
                }
            });
            valueAnimator.setInterpolator(mAnimationInterpolator);
            valueAnimator
                    .setDuration(mAnimationSpeed)
                    .start();

            return true;
        }

        return false;
    }

    public boolean collapse() {
        if (mExpanded && !mAnimating && mMaxLines >= 0) {
            notifyOnCollapse();
            final int expandedHeight = this.getMeasuredHeight();
            mAnimating = true;

            final ValueAnimator valueAnimator = ValueAnimator.ofInt(expandedHeight, mCollapsedHeight);
            valueAnimator.addUpdateListener(animation ->
                    setHeight((int) animation.getAnimatedValue()));

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    mExpanded = false;
                    mAnimating = false;

                    setMaxLines(mMaxLines);

                    final ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    setLayoutParams(layoutParams);
                }
            });

            valueAnimator.setInterpolator(mAnimationInterpolator);
            valueAnimator
                    .setDuration(mAnimationSpeed)
                    .start();

            return true;
        }

        return false;
    }

    private void notifyOnExpand() {
        if (mListener != null) {
            mListener.onExpand(this);
        }
    }

    private void notifyOnCollapse() {
        if (mListener != null) {
            mListener.onCollapse(this);
        }
    }

    public void setExpandListener(OnExpandListener listener) {
        mListener = listener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable parcelable = super.onSaveInstanceState();
        final SavedState ss = new SavedState(parcelable);
        ss.setExpanded(mExpanded);
        return ss;
    }

    @Override
    public void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mSavedState = ss;
    }

    private static class SavedState extends View.BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private boolean mExpanded;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mExpanded = in.readInt() == 1;
        }

        boolean getExpanded() {
            return mExpanded;
        }

        void setExpanded(boolean expanded) {
            mExpanded = expanded;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mExpanded ? 1 : 0);
        }


    }

    public interface OnExpandListener {
        /**
         * The {@link ExpandableTextView} is being expanded.
         *
         * @param view the textview
         */
        void onExpand(@NonNull ExpandableTextView view);

        /**
         * The {@link ExpandableTextView} is being collapsed.
         *
         * @param view the textview
         */
        void onCollapse(@NonNull ExpandableTextView view);
    }

}