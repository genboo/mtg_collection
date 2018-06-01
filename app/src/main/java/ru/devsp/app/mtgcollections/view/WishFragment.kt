package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_wish.*
import kotlinx.android.synthetic.main.nav_wish_filter.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.Filter
import ru.devsp.app.mtgcollections.model.objects.SetName
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import ru.devsp.app.mtgcollections.view.adapters.WishListAdapter
import ru.devsp.app.mtgcollections.viewmodel.WishViewModel
import java.util.*
import javax.inject.Inject

/**
 * Список хотелок
 * Created by gen on 21.12.2017.
 */

class WishFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var inflater: LayoutInflater

    private val filterOptions: Filter
        get() {
            val filter = Filter()
            filter.sets = getSelectedFilterItems(filterSetsBlock)
            return filter
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wish, container, false)
        initFragment()
        setHasOptionsMenu(true)
        this.inflater = inflater
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(WishViewModel::class.java)

        val adapter = WishListAdapter(null)
        val layoutManager = GridLayoutManager(context, 3)
        list.layoutManager = layoutManager
        list.adapter = adapter

        viewModel.wishList.observe(this,
                Observer { cards -> updateListData(adapter, cards) })

        viewModel.wishSetNames.observe(this,
                Observer { setNames ->
                    if (setNames != null) {
                        filterSetsBlock.removeAllViews()
                        val holder = LinearLayout(context)
                        holder.orientation = LinearLayout.VERTICAL
                        val selectedList = Arrays.asList(*viewModel.selectedFilter?.sets)
                        for (item: SetName in setNames) {
                            val filterView = inflater.inflate(R.layout.list_item_filter, holder, false)
                            val filterText = filterView.findViewById<CheckBox>(R.id.filter_selector)
                            filterText.text = item.setName
                            filterText.tag = item.setName
                            if(selectedList.contains(item.setName)){
                                filterText.isChecked = true
                            }
                            holder.addView(filterView)
                        }
                        filterSetsBlock.addView(holder)
                    }
                })
        viewModel.setFilter(viewModel.selectedFilter ?: Filter())

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<Card> {
            override fun click(position: Int, item: Card, view: View?) {
                navigation.toCard(item.id, view as ImageView)
            }
        })

        filterSelectOptions.setOnClickListener({ _ ->
            updateListData(adapter, null)
            viewModel.setFilter(filterOptions)
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        })

    }

    private fun getSelectedFilterItems(block: LinearLayout): Array<String> {
        val child = block.getChildAt(0) as LinearLayout
        val selected = ArrayList<String>()
        for (i in 0 until child.childCount) {
            val filterItem = child.getChildAt(i).findViewById<CheckBox>(R.id.filter_selector)
            if (filterItem.isChecked) {
                selected.add(filterItem.tag as String)
            }
        }
        return selected.toTypedArray()
    }

    private fun updateListData(adapter: WishListAdapter, items: List<Card>?) {
        if(adapter.itemCount == 0) {
            adapter.setItems(items ?: emptyList())
            if (list.isComputingLayout) {
                list.post { adapter.notifyDataSetChanged() }
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_wish, menu)
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return "Хочу!"
    }

}