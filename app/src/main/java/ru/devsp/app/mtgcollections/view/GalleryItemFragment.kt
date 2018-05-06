package ru.devsp.app.mtgcollections.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import ru.devsp.app.mtgcollections.MainActivity
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.tools.ImageLoader


/**
 * Фрагмент карточки
 * Created by gen on 09.10.2017.
 */

class GalleryItemFragment : Fragment() {

    private val args: Bundle by lazy { arguments ?: Bundle() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery_item, container, false)

        val image = view.findViewById<ImageView>(R.id.cardImage)
        ImageLoader.loadImageFromCache(image, args.getString(ARG_URL))
        image.setOnClickListener { _ -> (activity as MainActivity).navigation.toCard(args.getString(ARG_ID)) }
        return view
    }

    companion object {

        private const val ARG_URL = "url"
        private const val ARG_ID = "id"

        fun getInstance(url: String, id: String): GalleryItemFragment {
            val args = Bundle()
            args.putString(ARG_URL, url)
            args.putString(ARG_ID, id)
            val fragment = GalleryItemFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
