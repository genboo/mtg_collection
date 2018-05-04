package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.tools.Status
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryCard
import ru.devsp.app.mtgcollections.model.objects.Wish
import ru.devsp.app.mtgcollections.tools.ImageLoader
import ru.devsp.app.mtgcollections.tools.OracleReplacer
import ru.devsp.app.mtgcollections.view.adapters.LibrarySelectAdapter
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import ru.devsp.app.mtgcollections.view.adapters.ReprintListAdapter
import ru.devsp.app.mtgcollections.view.components.ExpandListener
import ru.devsp.app.mtgcollections.view.components.SetTextWatcher
import ru.devsp.app.mtgcollections.viewmodel.SearchViewModel
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var addDialog: AlertDialog? = null
    private var currentCard: Card? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initFragment()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        content = contentBlock
        progressBar = progressBlock

        if (savedInstanceState != null) {
            searchSet.setText(savedInstanceState.getString(STATE_SET))
            searchNum.setText(savedInstanceState.getString(STATE_NUMBER))
        }

        val model = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)

        searchSet.addTextChangedListener(SetTextWatcher(searchNum))

        observeSearch(model)

        cardRulings.setExpandListener(ExpandListener(cardRulesArrow))

        //Поиск карты
        search.setOnClickListener { _ ->
            hideSoftKeyboard()
            search(model)
        }

        if (savedInstanceState != null && savedInstanceState.getBoolean(STATE_SEARCH)) {
            search(model)
        }

        model.libraries.observe(this, Observer { libraries ->
            if (libraries != null && (libraries.isEmpty() || libraries[0].id != 0L)) {
                initAddDialog(model, arrayListOf(Library("")).plus(libraries))
            }
        })

        //Сохранение карты в коллекцию
        cardSave.setOnClickListener { _ -> showAddDialog() }

        //Есть карта в базе или нет
        observeExists(model)

        //Переход к карте
        goToCard.setOnClickListener { _ ->
            navigation.toCard(currentCard!!.id, cardImage)
        }

        //Добавление в виш лист
        toWishList.setOnClickListener { _ -> addToFavorite(model) }

        //Список репринтов
        val adapter = ReprintListAdapter(null)
        reprints.adapter = adapter

        val manager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        reprints.layoutManager = manager

        if (arguments != null) {
            model.search(arguments.getString(ARGS_SET), null, arguments.getString(ARGS_NAME))
        }

    }

    private fun observeExists(model: SearchViewModel) {
        model.cardExist.observe(this, Observer { card ->
            if (card == null || !card.wish && card.count == 0) {
                toWishList.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_heart_outline))
                goToCard.visibility = View.GONE
                cardCount.text = ""
                cardCount.visibility = View.GONE
            } else {
                currentCard!!.count = card.count
                goToCard.visibility = View.VISIBLE
                cardCount.text = String.format("%s", card.count)
                cardCount.visibility = View.VISIBLE
                if (card.wish) {
                    toWishList.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_heart))
                }
            }
        })
    }

    private fun addToFavorite(model: SearchViewModel) {
        model.save(currentCard)
        val wish = Wish()
        wish.cardId = currentCard!!.id
        model.addToWish(wish).observe(this, Observer { id ->
            if (id != null) {
                showSnack(R.string.action_added, View.OnClickListener { _ ->
                    wish.id = id
                    model.deleteWish(wish)
                })
            }
        })
    }

    private fun search(model: SearchViewModel) {
        if (!searchNum.text.toString().isEmpty()) {
            model.search(searchSet.text.toString(), searchNum.text.toString(), null)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(STATE_SET, searchSet?.text.toString())
        outState?.putString(STATE_NUMBER, searchNum?.text.toString())
        outState?.putBoolean(STATE_SEARCH, currentCard != null)
        super.onSaveInstanceState(outState)
    }

    private fun observeSearch(model: SearchViewModel) {
        model.card.observe(this, Observer { resource ->
            if (resource?.status == Status.LOADING) {
                showProgressBar()
            } else if (resource?.status == Status.SUCCESS && resource.data != null) {
                val card = resource.data.find {
                    resource.data.size == 1
                            || it.name == arguments.getString(ARGS_NAME)
                            || it.nameOrigin == arguments.getString(ARGS_NAME)
                }
                if (card != null) {
                    card.prepare()
                    model.checkCard(card.id)
                    updateSearchResult(card)
                }
                currentCard = card
                showContent()
            } else if (resource?.status == Status.ERROR || resource!!.data == null) {
                showToast("Ничего не найдено")
                showContent()
            }
        })
    }

    private fun showAddDialog() {
        addDialog?.show()
    }

    private fun initAddDialog(model: SearchViewModel, libraries: List<Library>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_card, mainBlock, false)
        val selector = dialogView.findViewById<Spinner>(R.id.spn_card_library)
        // адаптер
        val adapter = LibrarySelectAdapter(context, libraries)
        selector.adapter = adapter
        addDialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Добавить карту")
                .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("Ok") { _, _ ->
                    if (currentCard != null) {

                        val countText = dialogView.findViewById<EditText>(R.id.et_card_count)
                        var count = 1
                        if ("" != countText.text.toString()) {
                            count = Integer.valueOf(countText.text.toString())!!
                        }
                        currentCard!!.count = count

                        //Сохраняем локальную копию
                        model.save(currentCard)

                        val selectedLibrary = selector.selectedItem as Library
                        if (selectedLibrary.id != 0L) {
                            //Сохраняем в колоду
                            val item = LibraryCard()
                            item.cardId = currentCard!!.id
                            item.libraryId = selectedLibrary.id
                            item.count = count
                            model.addToLibrary(item)
                        }
                        showSnack(R.string.action_added, null)
                    }
                }
                .create()
    }

    private fun updateSearchResult(card: Card) {
        ViewCompat.setTransitionName(cardImage, card.id)
        ImageLoader.loadImageFromCache(context, this, cardImage, card.imageUrl)

        //Текст правил
        if (card.rulesText != null) {
            cardRulings.text = OracleReplacer.getText(card.rulesText, activity)
        }
        cardRulesTitle.setOnClickListener { _ -> cardRulings.toggle() }

        //Список репринтов
        (reprints.adapter as ReprintListAdapter).setItems(card.printings ?: emptyList())
        (reprints.adapter as ReprintListAdapter).setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<String> {
            override fun click(position: Int, item: String, view: View?) {
                navigation.toSearch(card.printings[position], card.nameOrigin)
            }
        })
        reprints.adapter.notifyDataSetChanged()
        reprints.post { reprints.adapter.notifyDataSetChanged() }
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return "Поиск"
    }

    companion object {

        private const val STATE_SET = "set"
        private const val STATE_NUMBER = "number"
        private const val STATE_SEARCH = "search"

        private const val ARGS_SET = "set"
        private const val ARGS_NAME = "name"


        fun getInstance(set: String, name: String): SearchFragment {

            val args = Bundle()
            args.putString(ARGS_SET, set)
            args.putString(ARGS_NAME, name)
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
