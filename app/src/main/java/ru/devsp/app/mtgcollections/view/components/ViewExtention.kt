package ru.devsp.app.mtgcollections.view.components

import android.os.Build
import android.transition.*
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

fun View.slideIn(viewGroup: ViewGroup, edge:Int){
    TransitionManager.beginDelayedTransition(viewGroup, Slide(edge))
    this.visibility = View.VISIBLE
}

fun View.slideOut(viewGroup: ViewGroup, edge:Int){
    TransitionManager.beginDelayedTransition(viewGroup, Slide(edge))
    this.visibility = View.GONE
}

fun ViewGroup.toggleSlideChilds(visibility: Int, edge: Int, vararg views: View) {
    val transition = TransitionSet()
    var delay: Long = 0
    for (view in views) {
        val slide = Slide(edge)
        slide.interpolator = AnticipateOvershootInterpolator()
        slide.addTarget(view).startDelay = delay
        transition.addTransition(slide)
        delay += 100
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        TransitionManager.endTransitions(this)
    }
    TransitionManager.beginDelayedTransition(this, transition)
    for (view in views) {
        view.visibility = visibility
    }
}


