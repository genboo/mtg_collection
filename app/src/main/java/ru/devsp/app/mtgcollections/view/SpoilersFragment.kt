package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_spoilers.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.tools.Status
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.repository.bound.CardsSetBound
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewScrollListener
import ru.devsp.app.mtgcollections.view.adapters.SpoilersListAdapter
import ru.devsp.app.mtgcollections.view.adapters.diffs.SpoilersDiffCallback
import ru.devsp.app.mtgcollections.viewmodel.SpoilersViewModel
import javax.inject.Inject


/**
 * Список спойлеров
 * Created by gen on 22.12.2017.
 */

class SpoilersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_spoilers, container, false)
        initFragment()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        val set = args.getString(ARG_SET)

        progressBar = progressBlock
        content = list

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SpoilersViewModel::class.java)

        val adapter = SpoilersListAdapter(null)
        val layoutManager = GridLayoutManager(context, 3)
        list.layoutManager = layoutManager
        list.adapter = adapter
        list.clearOnScrollListeners()
        list.addOnScrollListener(RecyclerViewScrollListener(object : RecyclerViewScrollListener.OnReactListener {
            override fun load(nextPage: Int) {
                viewModel.setParams(set, nextPage + 1)
            }
        }, CardsSetBound.PAGES_SIZE))

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == SpoilersListAdapter.TYPE_LOADING) layoutManager.spanCount else 1
            }
        }

        showProgressBar()
        viewModel.cards.observe(this, Observer { resource ->
            if (resource?.status == Status.SUCCESS || resource?.status == Status.ERROR) {
                adapter.setLoading(false)
            } else if (resource?.status == Status.LOADING) {
                adapter.setLoading(true)
            }

            if (resource?.status == Status.SUCCESS && resource.data != null) {
                for (card in resource.data) {
                    card.prepare()
                }
                updateAdapterItems(adapter, resource.data)
                adapter.notifyItemChanged(adapter.getSize())
                showContent()
            }
        })

        //Количество карт в коллекции
        viewModel.cardsBySet.observe(this, Observer { items ->
            viewModel.cardsBySet.removeObservers(this)
            adapter.setCards(items)
            list.post { adapter.notifyDataSetChanged() }
        })

        viewModel.setParams(set, 1)

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<Card> {
            override fun click(position: Int, item: Card, view: View?) {
                navigation.toSearch(item.set, item.nameOrigin)
            }
        })
    }

    private fun updateAdapterItems(adapter: SpoilersListAdapter, items: List<Card>) {
        val diffs = DiffUtil.calculateDiff(SpoilersDiffCallback(adapter.getItems(), items), !adapter.getItems().isEmpty())
        adapter.setItems(items)
        diffs.dispatchUpdatesTo(adapter)
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return args.getString(ARG_NAME)
    }

    companion object {

        private const val ARG_SET = "set"
        private const val ARG_NAME = "name"

        fun getInstance(set: String, name: String): SpoilersFragment {
            val args = Bundle()
            args.putString(ARG_SET, set)
            args.putString(ARG_NAME, name)
            val fragment = SpoilersFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
