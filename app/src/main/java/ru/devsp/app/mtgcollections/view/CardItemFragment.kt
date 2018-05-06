package ru.devsp.app.mtgcollections.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_gallery_item.*
import ru.devsp.app.mtgcollections.MainActivity
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.tools.ImageLoader


/**
 * Фрагмент карточки
 * Created by gen on 09.10.2017.
 */

class CardItemFragment : Fragment() {

    private val args: Bundle by lazy { arguments ?: Bundle() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_gallery_item, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ViewCompat.setTransitionName(cardImage, args.getString(ARG_URL))
        ImageLoader.loadImageFromCache(cardImage, args.getString(ARG_URL))
        cardImage.setOnClickListener { _ ->
            (activity as MainActivity).navigation.toFullScreenImage(args.getString(ARG_URL), "", args.getString(ARG_URL), cardImage)
        }
    }

    companion object {

        private const val ARG_URL = "url"

        fun getInstance(url: String): CardItemFragment {
            val args = Bundle()
            args.putString(ARG_URL, url)
            val fragment = CardItemFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
