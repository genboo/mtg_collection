package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_libraries.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryInfo
import ru.devsp.app.mtgcollections.view.adapters.LibrariesListAdapter
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import ru.devsp.app.mtgcollections.viewmodel.LibrariesViewModel
import javax.inject.Inject


/**
 * Колоды
 * Created by gen on 03.10.2017.
 */

class LibrariesFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var container: ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_libraries, container, false)
        this.container = container
        initFragment()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(LibrariesViewModel::class.java)

        val adapter = LibrariesListAdapter(null)
        val layoutManager = LinearLayoutManager(context)
        list.layoutManager = layoutManager
        list.adapter = adapter

        viewModel.allLibraries.observe(this, Observer { libraries ->
            adapter.setItems(libraries ?: emptyList())
            list.post { adapter.notifyDataSetChanged() }
        })

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<LibraryInfo> {
            override fun click(position: Int, item: LibraryInfo, view: View?) {
                navigation.toLibrary(item.id, item.name)
            }
        })

        libraryAdd.setOnClickListener { _ ->
            val view = layoutInflater.inflate(R.layout.dialog_add_library, container, false)
            val addDialog = AlertDialog.Builder(context)
                    .setView(view)
                    .setTitle("Добавить колоду")
                    .setPositiveButton("Ok") { _, _ ->
                        val libraryName = view.findViewById<EditText>(R.id.et_library_name)
                        val name = libraryName.text.toString()
                        if ("" != name) {
                            viewModel.add(Library(name))
                        }
                    }
            addDialog.show()
        }

    }


    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return "Колоды"
    }
}
