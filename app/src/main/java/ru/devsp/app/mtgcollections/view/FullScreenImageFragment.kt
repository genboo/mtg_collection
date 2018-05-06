package ru.devsp.app.mtgcollections.view

import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_full_screen_image.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.tools.ImageLoader

class FullScreenImageFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_full_screen_image, container, false)
        initFragment()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ViewCompat.setTransitionName(cardImage, args.getString(ARG_ID))
        ImageLoader.loadImageFromCache(this, cardImage, args.getString(ARG_URL))
        close.setOnClickListener({ _ -> fragmentManager?.popBackStack() })
        cardImage.setOnClickListener({ _ -> fragmentManager?.popBackStack() })
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    companion object {

        private const val ARG_ID = "id"
        private const val ARG_URL = "url"
        private const val ARG_TITLE = "title"

        fun getInstance(url: String, title: String, id: String): FullScreenImageFragment {
            val args = Bundle()

            val fragment = FullScreenImageFragment()
            args.putString(ARG_URL, url)
            args.putString(ARG_ID, id)
            args.putString(ARG_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }
}
