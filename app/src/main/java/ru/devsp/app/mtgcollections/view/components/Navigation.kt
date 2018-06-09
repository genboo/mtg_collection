package ru.devsp.app.mtgcollections.view.components

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewCompat
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.view.*

class Navigation(private val fragmentManager: FragmentManager) {

    fun toFirst() {
        navigate(CollectionFragment(), "collection", false)
    }

    fun toLibrary(id: Long, title: String) {
        navigate(LibraryFragment.getInstance(id, title), "library", true)
    }

    fun toCard(id: String, imageView: ImageView) {
        navigate(CardFragment.getInstance(id), "card", true, arrayListOf(imageView))
    }

    fun toCard(id: String) {
        navigate(CardFragment.getInstance(id), "card", true)
    }

    fun toSetSpoilers(set: String, name: String) {
        navigate(SpoilersFragment.getInstance(set, name), "spoilers", true)
    }

    fun toFullScreenImage(url: String, title: String, id: String, imageView: ImageView) {
        navigate(FullScreenImageFragment.getInstance(url, title, id), "full_screen_image", true, arrayListOf(imageView))
    }

    fun toSearch() {
        navigate(SearchFragment(), "search", false)
    }

    fun toSearch(set: String, name: String) {
        navigate(SearchFragment.getInstance(set, name), "search", true)
    }

    fun toCollection() {
        navigate(CollectionFragment(), "collection", false)
    }

    fun toLibraries() {
        navigate(LibrariesFragment(), "libraries", false)
    }

    fun toPlayers() {
        navigate(PlayersFragment(), "players", false)
    }

    fun toWishList() {
        navigate(WishFragment(), "wish_list", false)
    }

    fun toSets() {
        navigate(SetsFragment(), "sets", false)
    }

    fun toSettings() {
        navigate(SettingsFragment(), "settings", false)
    }


    /**
     * Общий метод перехода к новому фрагменту
     *
     * @param fragment
     * @param tag
     * @param back
     */
    private fun navigate(fragment: Fragment, tag: String, back: Boolean, shared: List<View>? = null) {

        val fragmentPrevious = fragmentManager.findFragmentById(R.id.content)
        if (!back) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val ft = fragmentManager.beginTransaction()

        if (back) {
            ft.addToBackStack(tag)
        }

        val transitionExit = getExitTransition()

        if (shared != null && !shared.isEmpty()) {
            for (view: View in shared) {
                ft.addSharedElement(view, ViewCompat.getTransitionName(view))
            }
        }

        fragmentPrevious?.exitTransition = transitionExit
        fragment.enterTransition = getEnterTransition()

        ft.replace(R.id.content, fragment)
        ft.commit()
    }

    private fun getEnterTransition(): TransitionSet {
        val transitionEnter = TransitionSet()
        transitionEnter.ordering = TransitionSet.ORDERING_TOGETHER

        val slideContent = Slide(Gravity.BOTTOM)
        slideContent.excludeTarget(R.id.app_bar, true)
        slideContent.excludeTarget(R.id.close, true)

        val slideAppBar = Slide(Gravity.TOP)
        slideAppBar.excludeTarget(R.id.main, true)
        slideAppBar.excludeTarget(R.id.swipeRefresh, true)
        slideAppBar.excludeTarget(R.id.list, true)

        transitionEnter.addTransition(slideContent)
        transitionEnter.addTransition(slideAppBar)

        return transitionEnter
    }

    private fun getExitTransition(): TransitionSet {
        val transitionExit = TransitionSet()
        transitionExit.ordering = TransitionSet.ORDERING_TOGETHER

        val fade = Fade()
        transitionExit.addTransition(fade)

        return transitionExit
    }

}