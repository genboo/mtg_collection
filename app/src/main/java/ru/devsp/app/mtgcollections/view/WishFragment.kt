package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_wish.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import ru.devsp.app.mtgcollections.view.adapters.WishListAdapter
import ru.devsp.app.mtgcollections.viewmodel.WishViewModel
import javax.inject.Inject

/**
 * Список хотелок
 * Created by gen on 21.12.2017.
 */

class WishFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wish, container, false)
        initFragment()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(WishViewModel::class.java)

        val adapter = WishListAdapter(null)
        val layoutManager = GridLayoutManager(context, 2)
        list.layoutManager = layoutManager
        list.adapter = adapter

        viewModel.wishList.observe(this,
                Observer { cards -> updateListData(adapter, cards) })

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<Card> {
            override fun click(position: Int, item: Card, view: View?) {
                navigation.toCard(item.id, view as ImageView)
            }
        })

    }

    private fun updateListData(adapter: WishListAdapter, items: List<Card>?) {
        if (items != null) {
            adapter.setItems(items)
            if (list.isComputingLayout) {
                list.post { adapter.notifyDataSetChanged() }
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return "Хочу!"
    }

}