package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_gallery.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Filter
import ru.devsp.app.mtgcollections.view.adapters.GalleryPagerAdapter
import ru.devsp.app.mtgcollections.view.components.GalleryPageTransform
import ru.devsp.app.mtgcollections.viewmodel.CollectionViewModel
import javax.inject.Inject

/**
 * Коллекция карт
 * Created by gen on 03.10.2017.
 */

class GalleryFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        initFragment()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        cardsGallery.setPageTransformer(true, GalleryPageTransform())

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(CollectionViewModel::class.java)
        val adapter = GalleryPagerAdapter(childFragmentManager, null)
        viewModel.filteredCards.observe(this, Observer { cards ->
            if(cards != null) {
                adapter.setItems(cards)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.filter.observe(this, Observer { filter ->
            if(filter != null) {
                viewModel.setFilter(filter)
                val selected = Filter()
                selected.colors = args.getStringArray(ARG_COLORS)
                selected.sets = args.getStringArray(ARG_SETS)
                selected.rarities = args.getStringArray(ARG_RARITIES)
                selected.types = args.getStringArray(ARG_TYPES)
                selected.subtypes = args.getStringArray(ARG_SUBTYPES)
                viewModel.setFilter(selected)
            }
        })
        cardsGallery.adapter = adapter
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return "Коллекция"
    }

    companion object {

        private const val ARG_TYPES = "types"
        private const val ARG_SUBTYPES = "subtypes"
        private const val ARG_COLORS = "colors"
        private const val ARG_SETS = "sets"
        private const val ARG_RARITIES = "rarities"

        fun getInstance(filter: Filter): GalleryFragment {
            val args = Bundle()
            args.putStringArray(ARG_COLORS, filter.colors)
            args.putStringArray(ARG_TYPES, filter.types)
            args.putStringArray(ARG_SUBTYPES, filter.subtypes)
            args.putStringArray(ARG_SETS, filter.sets)
            args.putStringArray(ARG_RARITIES, filter.rarities)
            val fragment = GalleryFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
