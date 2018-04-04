package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_library.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryInfo
import ru.devsp.app.mtgcollections.view.adapters.CardListItem
import ru.devsp.app.mtgcollections.view.adapters.CardsLibraryListAdapter
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import ru.devsp.app.mtgcollections.viewmodel.CollectionViewModel
import javax.inject.Inject


/**
 * Список карт колы
 * Created by gen on 03.10.2017.
 */

class LibraryFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var container: ViewGroup? = null

    private var mEditDialog: AlertDialog.Builder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)
        this.container = container
        initFragment()
        setHasOptionsMenu(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()
        list.isNestedScrollingEnabled = true
        val title = arguments.getString(ARG_TITLE)
        val id = arguments.getLong(ARG_ID, 0)
        updateTitle(title!!)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(CollectionViewModel::class.java)

        val adapter = CardsLibraryListAdapter(null)
        val layoutManager = LinearLayoutManager(context)
        list.layoutManager = layoutManager
        list.adapter = adapter

        viewModel.cardsByLibrary.observe(this, Observer { cards ->
            adapter.setCards(cards ?: emptyList())
            list.post { adapter.notifyDataSetChanged() }
        })

        viewModel.cardsManaState.observe(this, Observer { states -> libraryState.setManaState(states) })
        viewModel.cardsColorState.observe(this, Observer { states -> libraryState.setColorState(states) })

        viewModel.setLibrary(id)

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<CardListItem> {
            override fun click(position: Int, item: CardListItem, view: View?) {
                navigation.toCard(item.card.id, view as ImageView)
            }
        })

        val view = layoutInflater.inflate(R.layout.dialog_add_library, container, false)
        val libraryName = view.findViewById<EditText>(R.id.et_library_name)
        libraryName.setText(title)
        mEditDialog = AlertDialog.Builder(context)
                .setView(view)
                .setTitle("Редактировать колоду")
                .setPositiveButton("Ok") { _, _ ->

                    val name = libraryName.text.toString()
                    if ("" != name) {
                        val lib = Library(id)
                        lib.name = name
                        viewModel.updateLibrary(lib)
                        updateTitle(name)
                    }
                }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.nav_edit) {
            mEditDialog!!.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_library, menu)
    }


    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return ""
    }

    companion object {

        private const val ARG_ID = "id"
        private const val ARG_TITLE = "title"

        fun getInstance(id: Long, title: String): LibraryFragment {
            val args = Bundle()
            args.putLong(ARG_ID, id)
            args.putString(ARG_TITLE, title)
            val fragment = LibraryFragment()
            fragment.arguments = args
            return fragment
        }
    }


}