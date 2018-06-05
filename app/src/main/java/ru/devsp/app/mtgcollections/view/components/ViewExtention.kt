package ru.devsp.app.mtgcollections.view.components

import android.transition.*
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator

fun View.fadeIn(viewGroup: ViewGroup) {
    TransitionManager.beginDelayedTransition(viewGroup, Fade())
    this.visibility = View.VISIBLE
}

fun View.fadeOut(viewGroup: ViewGroup) {
    TransitionManager.beginDelayedTransition(viewGroup, Fade())
    this.visibility = View.GONE
}

fun View.slideIn(viewGroup: ViewGroup) {
    TransitionManager.beginDelayedTransition(viewGroup, Slide(Gravity.BOTTOM))
    this.visibility = View.VISIBLE
}

fun View.slideInEnd(viewGroup: ViewGroup) {
    val slide = Slide(Gravity.END)
    slide.interpolator = AnticipateOvershootInterpolator()
    TransitionManager.beginDelayedTransition(viewGroup, slide)
    this.visibility = View.VISIBLE
}

fun View.slideOutEnd(viewGroup: ViewGroup) {
    val slide = Slide(Gravity.END)
    slide.interpolator = AnticipateOvershootInterpolator()
    TransitionManager.beginDelayedTransition(viewGroup, slide)
    this.visibility = View.GONE
}

fun View.toggleSlideEnd(viewGroup: ViewGroup) {
    val slide = Slide(Gravity.END)
    slide.interpolator = AnticipateOvershootInterpolator()
    TransitionManager.beginDelayedTransition(viewGroup, slide)

    if (this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
    }
}

fun View.toggle(viewGroup: ViewGroup) {
    val transition = TransitionSet()
    transition.addTransition(Fade())
    transition.ordering = TransitionSet.ORDERING_TOGETHER
    TransitionManager.beginDelayedTransition(viewGroup, transition)

    if (this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
    }
}


