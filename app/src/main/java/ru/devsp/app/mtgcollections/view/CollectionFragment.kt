package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.WorkerThread
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_collection.*
import kotlinx.android.synthetic.main.nav_filter.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.Filter
import ru.devsp.app.mtgcollections.tools.AnimatorHelper
import ru.devsp.app.mtgcollections.tools.AppExecutors
import ru.devsp.app.mtgcollections.view.adapters.CardsListAdapter
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import ru.devsp.app.mtgcollections.viewmodel.CollectionViewModel
import java.util.*
import javax.inject.Inject

class CollectionFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutor: AppExecutors

    lateinit var inflater: LayoutInflater

    private val filterViewData: MutableLiveData<BlockHolder> = MutableLiveData()

    private val filterOptions: Filter
        get() {
            val filter = Filter()
            filter.types = getSelectedFilterItems(filterTypesBlock)
            filter.subtypes = getSelectedFilterItems(filterSubtypesBlock)
            filter.colors = getSelectedFilterItems(filterColorsBlock)
            filter.sets = getSelectedFilterItems(filterSetsBlock)
            filter.rarities = getSelectedFilterItems(filterRarityBlock)
            return filter
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)
        this.inflater = inflater
        initFragment()
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(CollectionViewModel::class.java)

        filterType.setOnClickListener { _ -> switchBlockVisibility(filterTypesBlock) }
        filterSubtype.setOnClickListener { _ -> switchBlockVisibility(filterSubtypesBlock) }
        filterColor.setOnClickListener { _ -> switchBlockVisibility(filterColorsBlock) }
        filterSets.setOnClickListener { _ -> switchBlockVisibility(filterSetsBlock) }
        filterRarity.setOnClickListener { _ -> switchBlockVisibility(filterRarityBlock) }

        val adapter = CardsListAdapter(null)
        val layoutManager = LinearLayoutManager(context)
        list.layoutManager = layoutManager
        list.adapter = adapter

        viewModel.filteredCards.observe(this, Observer { cards -> updateListData(adapter, cards) })

        viewModel.filter.observe(this, Observer { filter ->
            val selected = viewModel.selectedFilter
            appExecutor.diskIO().execute { initFilter(filter, selected) }
            if (selected == null) {
                //Показываем все карты
                if (filter != null) {
                    filter.full = true
                    viewModel.setFilter(filter)
                }
            } else {
                //Есть выбранные фильтры
                viewModel.setFilter(selected)
            }
        })

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<Card> {
            override fun click(position: Int, item: Card, view: View?) {
                navigation.toCard(item.id, view as ImageView)
            }
        })

        filterSelectOptions.setOnClickListener { _ ->
            updateListData(adapter, null)
            viewModel.setFilter(filterOptions)
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }

        filterViewData.observe(this, Observer { data ->
            if (data != null && data.holder.parent == null) {
                attachToView(data.block, data.holder, data.items, data.selected)
            }
        })
    }

    private fun updateListData(adapter: CardsListAdapter, items: List<Card>?) {
        adapter.setItems(items ?: emptyList())
        if (list.isComputingLayout) {
            list.post { adapter.notifyDataSetChanged() }
        } else {
            adapter.notifyDataSetChanged()
        }
    }

    private fun switchBlockVisibility(block: View) {
        if (block.visibility == View.VISIBLE) {
            AnimatorHelper.animate(context, block, R.anim.fade_out, View.GONE)
        } else {
            AnimatorHelper.animate(context, block, R.anim.fade_in, View.VISIBLE)
        }
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

    /**
     * Фильтры собираем в фоновом потоке, т.к. их может быть очень много
     *
     * @param filter   Список всех фильтров
     * @param selected Список выбранных фильтров
     */
    @WorkerThread
    private fun initFilter(filter: Filter?, selected: Filter?) {
        if (filter != null) {
            makeFilterOptions(filterColorsBlock, filter.colors, selected?.colors ?: arrayOfNulls(0))
            makeFilterOptions(filterTypesBlock, filter.types, selected?.types ?: arrayOfNulls(0))
            makeFilterOptions(filterSubtypesBlock, filter.subtypes, selected?.subtypes
                    ?: arrayOfNulls(0))
            makeFilterOptions(filterSetsBlock, filter.sets, selected?.sets ?: arrayOfNulls(0))
            makeFilterOptions(filterRarityBlock, filter.rarities, selected?.rarities
                    ?: arrayOfNulls(0))
            appExecutor.mainThread().execute({ filterViewData.value = null })
        }
    }

    /**
     * Создание блока фильтров
     *
     * @param block  Родительский блок
     * @param items    список фильтров
     * @param selected список выбранных фильтров
     */
    private fun makeFilterOptions(block: LinearLayout?, items: Array<String>, selected: Array<String?>) {
        if (context != null) {
            val holder = LinearLayout(context)
            holder.orientation = LinearLayout.VERTICAL
            for (item in items) {
                val filterView = inflater.inflate(R.layout.list_item_filter, holder, false)
                val filterText = filterView.findViewById<CheckBox>(R.id.filter_selector)
                filterText.text = item
                filterText.tag = item
                holder.addView(filterView)
            }

            //Результат подключаем во view в главном потоке
            //Там же отмечаем выбранные фильтры, т.к. это запускает анимацию
            appExecutor.mainThread()
                    .execute { filterViewData.value = BlockHolder(block, holder, items, selected) }
        }
    }

    /**
     *
     * @param block   Родительский блок
     * @param holder    холдер
     * @param items     список фильтров
     * @param selected  список выбранных фильтров
     */
    private fun attachToView(block: LinearLayout?, holder: LinearLayout, items: Array<String>, selected: Array<String?>) {
        var haveSelectedItem = false
        val selectedList = Arrays.asList(*selected)
        for (i in 0 until holder.childCount) {
            val item = holder.getChildAt(i).findViewById<CheckBox>(R.id.filter_selector)
            if (selected.size != items.size && selectedList.contains(item.tag.toString())) {
                item.isChecked = true
                haveSelectedItem = true
            }
        }

        block?.addView(holder)
        if (haveSelectedItem) {
            block?.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.nav_gallery) {
            navigation.toGallery(filterOptions)
        } else if (item.itemId == R.id.filter) {
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
        inflater.inflate(R.menu.fragment_collection, menu)
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return "Коллекция"
    }


    class BlockHolder(val block: LinearLayout?, val holder: LinearLayout, val items: Array<String>, val selected: Array<String?>)
}