package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_sets.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.tools.Status
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.model.tools.Resource
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import ru.devsp.app.mtgcollections.view.adapters.SetsListAdapter
import ru.devsp.app.mtgcollections.viewmodel.SetsViewModel
import javax.inject.Inject

class SetsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sets, container, false)
        initFragment()
        setHasOptionsMenu(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        progressBar = progressBlock
        content = list

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetsViewModel::class.java)

        val adapter = SetsListAdapter(null)
        val manager = LinearLayoutManager(context)
        list.adapter = adapter
        list.layoutManager = manager

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<Set> {
            override fun click(position: Int, item: Set, view: View?) {
                navigation.toSetSpoilers(item.code, item.name)
            }
        })

        showProgressBar()
        viewModel.sets.observe(this, Observer { resource ->
            if (resource?.status == Status.SUCCESS) {
                if (resource.data != null) {
                    adapter.setItems(resource.data.sortedByDescending { it.releaseDate })
                    list.post { adapter.notifyDataSetChanged() }
                }
                showContent()
            } else if (resource?.status == Status.ERROR) {
                showToast(resource.message ?: "", Toast.LENGTH_SHORT)
                showContent()
            }
        })

    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return "Сеты"
    }

}
