package ru.devsp.app.mtgcollections.view

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.transition.TransitionInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_app_bar_main.*
import ru.devsp.app.mtgcollections.App
import ru.devsp.app.mtgcollections.MainActivity
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.di.components.AppComponent
import ru.devsp.app.mtgcollections.view.components.Navigation


abstract class BaseFragment : Fragment() {

    var progressBar: View? = null
    var content: View? = null

    var component: AppComponent? = null
    internal val navigation: Navigation by lazy { (activity as MainActivity).navigation }
    val args: Bundle by lazy { arguments ?: Bundle() }


    protected fun updateToolbar() {
        if (toolbar != null) {
            toolbar.title = getTitle()
            (activity as MainActivity).updateActionBar(toolbar)
            (activity as MainActivity).updateDrawer(toolbar)
        }
    }

    protected fun initFragment() {
        if (component == null) {
            component = (activity?.application as App).appComponent
            inject()
        }
    }

    internal abstract fun inject()

    abstract fun getTitle(): String


    protected fun hideSoftKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    protected fun showToast(message: String, length: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, length).show()
    }

    protected fun showSnack(text: Int, action: View.OnClickListener?) {
        if (view != null) {
            val snackBar = Snackbar.make(view!!, text, Snackbar.LENGTH_SHORT)
            if (action != null) {
                snackBar.setAction(R.string.action_cancel, action)
            }
            snackBar.show()
        }
    }

    fun showProgressBar() {
        content?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    fun showContent() {
        progressBar?.visibility = View.GONE
        content?.visibility = View.VISIBLE
    }

    fun updateTitle(title: String) {
        toolbar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (lifecycle as? LifecycleRegistry)?.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (lifecycle as? LifecycleRegistry)?.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}