package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.*
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
import ru.devsp.app.mtgcollections.view.components.*
import ru.devsp.app.mtgcollections.viewmodel.SearchViewModel
import java.util.*
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var addDialog: AlertDialog? = null
    private var currentCard: Card? = null

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initFragment()
        setHasOptionsMenu(true)
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

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)

        searchSet.addTextChangedListener(SetTextWatcher(searchNum))

        observeSearch()

        cardRulings.setExpandListener(ExpandListener(cardRulesArrow))

        if (savedInstanceState != null && savedInstanceState.getBoolean(STATE_SEARCH)) {
            search()
        }

        viewModel.libraries.observe(this, Observer { libraries ->
            if (libraries != null && (libraries.isEmpty() || libraries[0].id != 0L)) {
                initAddDialog(arrayListOf(Library("")).plus(libraries))
            }
        })

        //Сохранение карты в коллекцию
        cardSave.setOnClickListener { _ -> showAddDialog() }

        //Есть карта в базе или нет
        observeExists()

        //Переход к карте
        goToCard.setOnClickListener { navigation.toCard(currentCard!!.id, cardImage) }

        //Добавление в виш лист
        toWishList.setOnClickListener { _ -> addToFavorite() }

        //Список репринтов
        val adapter = ReprintListAdapter(null)
        reprints.adapter = adapter

        val manager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        reprints.layoutManager = manager

        if (arguments != null) {
            viewModel.search(args.getString(ARGS_SET), null, args.getString(ARGS_NAME))
        }

    }

    private fun observeExists() {
        viewModel.cardExist.observe(this, Observer { card ->
            currentCard!!.count = card?.count ?: 0
            if (card == null || (!card.wish && card.count == 0)) {
                toWishList.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart_outline))
                toWishList.tag = null
                actionsBlock.toggleSlideChilds(View.GONE, Gravity.END, cardCount, goToCard)
            } else {
                cardCount.text = String.format("%s", card.count)
                actionsBlock.toggleSlideChilds(View.VISIBLE, Gravity.END, goToCard, cardCount)
                if (card.wish) {
                    toWishList.tag = card.id
                    toWishList.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart))
                } else {
                    toWishList.tag = null
                    toWishList.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart_outline))
                }
            }
        })
    }

    private fun addToFavorite() {
        if (currentCard != null) {
            if (toWishList.tag == null) {
                viewModel.save(currentCard)
                val wish = Wish()
                wish.cardId = currentCard?.id
                viewModel.addToWish(wish)
            } else {
                val wish = Wish()
                wish.id = toWishList.tag.toString().toLong()
                viewModel.deleteWish(wish)
            }
        }
    }

    private fun search() {
        if (!searchNum.text.toString().isEmpty()) {
            viewModel.search(searchSet.text.toString(), searchNum.text.toString(), null)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_SET, searchSet?.text.toString())
        outState.putString(STATE_NUMBER, searchNum?.text.toString())
        outState.putBoolean(STATE_SEARCH, currentCard != null)
        super.onSaveInstanceState(outState)
    }

    private fun observeSearch() {
        viewModel.card.observe(this, Observer { resource ->
            if (resource?.status == Status.LOADING) {
                showProgressBar()
            } else if (resource?.status == Status.SUCCESS && resource.data != null) {
                val card = resource.data.find {
                    resource.data.size == 1
                            || it.name == args.getString(ARGS_NAME)
                            || it.nameOrigin == args.getString(ARGS_NAME)
                }
                if (card != null) {
                    card.prepare()
                    viewModel.checkCard(card.id)
                    updateSearchResult(card)
                }
                currentCard = card
                showContent()
            } else if (resource?.status == Status.ERROR || resource?.data == null) {
                showToast("Ничего не найдено")
                showContent()
            }
        })
    }

    private fun showAddDialog() {
        addDialog?.show()
    }

    private fun initAddDialog(libraries: List<Library>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_card, contentBlock, false)
        val selector = dialogView.findViewById<Spinner>(R.id.spn_card_library)
        val countText = dialogView.findViewById<NumberCounterView>(R.id.counterBlock)
        countText.setOnCounterClickListener { inc ->
            var count = countText.getCount().toInt()
            if (inc) {
                count++
            } else if (count > 1) {
                count--
            }
            countText.setCount(String.format(Locale.getDefault(), "%d", count))
        }

        // адаптер
        val adapter = LibrarySelectAdapter(requireContext(), libraries)
        selector.adapter = adapter
        addDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle("Добавить карту")
                .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("Ok") { _, _ ->
                    if (currentCard != null) {
                        currentCard!!.count = countText.getCount().toInt()

                        //Сохраняем локальную копию
                        viewModel.save(currentCard)

                        val selectedLibrary = selector.selectedItem as Library
                        if (selectedLibrary.id != 0L) {
                            //Сохраняем в колоду
                            val item = LibraryCard()
                            item.cardId = currentCard!!.id
                            item.libraryId = selectedLibrary.id
                            item.count = countText.getCount().toInt()
                            viewModel.addToLibrary(item)
                        }
                        showSnack(R.string.action_added, null)
                    }
                }
                .create()
    }

    private fun updateSearchResult(card: Card) {
        ViewCompat.setTransitionName(cardImage, card.id)
        ImageLoader.loadImageFromCache(cardImage, card.imageUrl)

        //Текст правил
        if (card.rulesText != null) {
            cardRulings.text = OracleReplacer.getText(card.rulesText, requireActivity())
        }
        cardRulesTitle.setOnClickListener { _ -> cardRulings.toggle() }

        //Список репринтов
        (reprints.adapter as ReprintListAdapter).setItems(card.printings ?: emptyList())
        (reprints.adapter as ReprintListAdapter).setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<String> {
            override fun click(position: Int, item: String, view: View?) {
                navigation.toSearch(item, card.nameOrigin)
            }
        })
        reprints?.post { reprints?.adapter?.notifyDataSetChanged() }
        searchNum.setText(card.number)
        searchSet.setText(card.set)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            search()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_search, menu)
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return ""
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
