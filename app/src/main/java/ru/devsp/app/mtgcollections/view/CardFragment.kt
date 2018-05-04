package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.transition.TransitionInflater
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.fragment_card.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.tools.Resource
import ru.devsp.app.mtgcollections.model.tools.Status
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.CardLibraryInfo
import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryCard
import ru.devsp.app.mtgcollections.tools.ImageLoader
import ru.devsp.app.mtgcollections.tools.OracleReplacer
import ru.devsp.app.mtgcollections.view.adapters.LibrarySelectAdapter
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import ru.devsp.app.mtgcollections.view.adapters.ReprintListAdapter
import ru.devsp.app.mtgcollections.view.components.ExpandListener
import ru.devsp.app.mtgcollections.viewmodel.CardViewModel
import java.util.*
import javax.inject.Inject

/**
 * Карта
 * Created by gen on 03.10.2017.
 */

class CardFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var localCard: Card? = null
    private var libraries: List<Library>? = null

    private var linkButton: MenuItem? = null
    private var addParentDialog: AlertDialog? = null
    private var addDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        initFragment()
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()
        ViewCompat.setTransitionName(cardImage, arguments.getString(ARG_ID))

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(CardViewModel::class.java)

        viewModel.card.observe(this,
                Observer { card -> updateCardInfo(viewModel, card) })

        viewModel.cardNetwork.observe(this,
                Observer { resource -> observeSearch(viewModel, resource) })

        viewModel.librariesByCard.observe(this,
                Observer { libraries -> updateLibrariesUi(viewModel, libraries) })

        viewModel.cardSide.observe(this,
                Observer { updateSideCard(it) })

        viewModel.setId(arguments.getString(ARG_ID, ""))

        viewModel.libraries.observe(this,
                Observer { libraries ->
                    this.libraries = libraries
                    if (libraries != null && (libraries.isEmpty() || libraries[0].id != 0L)) {
                        this.libraries = arrayListOf(Library("")).plus(libraries)
                    }
                    initAddToLibraryDialog(viewModel)
                })

        cardMultiId.setOnClickListener { _ ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("multivers id", cardMultiId.text)
            clipboard.primaryClip = clip
            showToast("Скопировано в буфер обмена", Toast.LENGTH_SHORT)
        }

        //Сохранение карты в коллекцию
        addToLibrary.setOnClickListener { _ -> showAddDialog() }

        val adapter = ReprintListAdapter(null)
        val manager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        reprints.adapter = adapter
        reprints.layoutManager = manager
    }

    private fun showAddDialog() {
        if (addDialog != null) {
            addDialog?.show()
        }
    }

    /**
     * Ослеживание поиска карты и ее обновление
     *
     * @param model    модель
     * @param resource результат поиска
     */
    private fun observeSearch(model: CardViewModel, resource: Resource<List<Card>>?) {
        if (resource != null && resource.status == Status.SUCCESS && resource.data != null) {
            val card = resource.data[0]
            card.prepare()
            if (localCard != null && card != localCard) {
                //Обновляем только если появились отличия
                card.count = localCard!!.count
                model.updateCard(card)
            }
            (reprints.adapter as ReprintListAdapter).setItems(card.printings ?: emptyList())
            (reprints.adapter as ReprintListAdapter).setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener<String> {
                override fun click(position: Int, item: String, view: View?) {
                    navigation.toSearch(item, card.nameOrigin)
                }
            })
            reprints?.post { reprints.adapter.notifyDataSetChanged() }

            if (resource.data.size > 1) {
                val secondCard = resource.data[1]
                secondCard.prepare()
                updateSideCard(secondCard)
            }
        }
    }

    private fun initAddToLibraryDialog(model: CardViewModel) {
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
                    if (localCard != null) {
                        val countText = dialogView.findViewById<EditText>(R.id.et_card_count)
                        var count = 1
                        if ("" != countText.text.toString()) {
                            count = Integer.valueOf(countText.text.toString())!!
                        }
                        val selectedLibrary = selector.selectedItem as Library
                        if (selectedLibrary.id != 0L) {
                            //Сохраняем в колоду
                            val item = LibraryCard()
                            item.cardId = localCard!!.id
                            item.libraryId = selectedLibrary.id
                            item.count = count
                            model.addToLibrary(item)
                        }
                        showSnack(R.string.action_added, null)
                    }
                }
                .create()
    }

    private fun updateSideCard(card: Card?) {
        if (card != null) {
            ViewCompat.setTransitionName(cardImageSecond, card.name + card.id)
            ImageLoader.loadImageFromCache(context, cardImageSecond, card.imageUrl)
            cardImageSecond.setOnClickListener { _ ->
                navigation.toFullScreenImage(card.imageUrl, card.name, card.name + card.id, cardImageSecond)
            }
            updateCardText(card)
        }
    }

    private fun updateCardText(card: Card) {
        val stringBuilder = SpannableStringBuilder()
        if (cardOracle.text.isNotEmpty()) {
            stringBuilder.append(cardOracle.text).append("\n\n")
                    .append(card.name).append("\n")
        }
        if (card.text != null) {
            stringBuilder.append(OracleReplacer.getText(card.text, activity))
        }
        val flavorStart = stringBuilder.length
        if (card.flavor != null) {
            stringBuilder.append("\n\n").append(card.flavor)
        }
        stringBuilder.setSpan(StyleSpan(Typeface.ITALIC),
                if (card.text == null) 0 else flavorStart,
                stringBuilder.length, 0)
        cardOracle.text = stringBuilder
    }

    private fun updateCardInfo(viewModel: CardViewModel, card: Card?) {
        localCard = card
        if (card != null) {
            ImageLoader.loadImageFromCache(context, this, cardImage, card.imageUrl)
            if (!card.child && card.parent != null) {
                viewModel.setIdChild(card.parent)
                if (linkButton != null) {
                    //Кнопки может еще не быть
                    linkButton!!.isVisible = true
                }
            }
            initAddDialog(viewModel)
            cardOracle.text = ""
            updateCardText(card)
            cardName.setOnClickListener { _ -> cardOracle.toggle() }
            cardName.text = card.name

            cardManaCost.text = OracleReplacer.getText(card.manaCost ?: "", activity)

            if (view != null) {
                cardOracle.setExpandListener(ExpandListener(cardOracleArrow))
                cardRules.setExpandListener(ExpandListener(cardRulesArrow))
            }
            cardRulesTitle.setOnClickListener { _ -> cardRules.toggle() }
            cardRules.text = OracleReplacer.getText(card.rulesText ?: "", activity)

            cardMultiId.text = card.id

            cardRarity.setColorFilter(ContextCompat.getColor(context, card.rarityColor), PorterDuff.Mode.SRC_IN)
            cardRarity.setImageDrawable(resources.getDrawable(card.setIcon, context.theme))

            updateTitle(card.name)

            countBlock.removeAllViews()
            val view = layoutInflater.inflate(R.layout.layout_card_library, countBlock, false)
            val name = view.findViewById<TextView>(R.id.tv_library_name)
            name.text = "Всего"
            val count = view.findViewById<TextView>(R.id.tv_library_cards_count)
            count.text = String.format(Locale.getDefault(), "%d", card.count)
            countBlock.addView(view)

            val task = { viewModel.updateCard(card) }
            val plus = view.findViewById<ImageButton>(R.id.ib_library_count_plus)
            plus.setOnClickListener { _ ->
                card.count++
                count.text = String.format(Locale.getDefault(), "%d", card.count)
                mainBlock.removeCallbacks(task)
                mainBlock.postDelayed(task, 400)
            }
            val minus = view.findViewById<ImageButton>(R.id.ib_library_count_minus)
            minus.setOnClickListener { _ ->
                if (card.count > 0) {
                    card.count--
                    count.text = String.format(Locale.getDefault(), "%d", card.count)
                    mainBlock.removeCallbacks(task)
                    mainBlock.postDelayed(task, 400)
                }
            }

            cardImage.setOnClickListener { _ ->
                navigation.toFullScreenImage(card.imageUrl, card.name, card.id, cardImage)
            }
        }
    }

    private fun updateLibrariesUi(viewModel: CardViewModel, libraries: List<CardLibraryInfo>?) {
        librariesBlock.removeAllViews()
        if (libraries != null) {
            for (item in libraries) {
                val view = layoutInflater.inflate(R.layout.layout_card_library, librariesBlock, false)
                val name = view.findViewById<TextView>(R.id.tv_library_name)
                name.text = item.name
                val count = view.findViewById<TextView>(R.id.tv_library_cards_count)
                count.text = String.format(Locale.getDefault(), "%d", item.count)
                librariesBlock.addView(view)
                val libraryCard = getLibraryCard(item)
                val task = { viewModel.saveCount(libraryCard) }
                val plus = view.findViewById<ImageButton>(R.id.ib_library_count_plus)
                plus.setOnClickListener { _ ->
                    libraryCard.count++
                    count.text = String.format(Locale.getDefault(), "%d", libraryCard.count)
                    mainBlock.removeCallbacks(task)
                    mainBlock.postDelayed(task, 400)
                }
                val minus = view.findViewById<ImageButton>(R.id.ib_library_count_minus)
                minus.setOnClickListener { _ ->
                    if (item.count > 0) {
                        libraryCard.count--
                        count.text = String.format(Locale.getDefault(), "%d", libraryCard.count)
                        mainBlock.removeCallbacks(task)
                        mainBlock.postDelayed(task, 400)
                    }
                }
            }
        }
    }

    /**
     * Формирование объекта привязки карты к колоде
     *
     * @param item карта
     * @return карта
     */
    private fun getLibraryCard(item: CardLibraryInfo): LibraryCard {
        val libraryCard = LibraryCard()
        libraryCard.id = item.id
        libraryCard.count = item.count
        libraryCard.libraryId = item.libraryId
        libraryCard.cardId = item.cardId
        return libraryCard
    }

    private fun initAddDialog(model: CardViewModel) {
        addParentDialog = AlertDialog.Builder(context)
                .setView(R.layout.dialog_add_parent)
                .setTitle("Указать вторую сторону")
                .setNegativeButton("Отмена") { d, _ -> d.dismiss() }
                .setPositiveButton("Ok") { _, _ ->
                    if (localCard != null) {
                        val parentId = addParentDialog!!.findViewById<EditText>(R.id.et_card_id)
                        if (parentId != null && "" != parentId.text.toString()) {
                            localCard!!.parent = parentId.text.toString()
                            model.updateLink(localCard!!.id, parentId.text.toString())
                            showToast("Обновлено", Toast.LENGTH_SHORT)
                        }
                    }
                }
                .create()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.nav_link) {
            addParentDialog!!.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_card, menu)
        linkButton = menu!!.findItem(R.id.nav_link)
        if (localCard != null && localCard!!.child) {
            linkButton!!.isVisible = false
        }
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    companion object {

        private const val ARG_ID = "id"

        fun getInstance(id: String): CardFragment {
            val args = Bundle()
            args.putString(ARG_ID, id)
            val fragment = CardFragment()
            fragment.arguments = args
            return fragment
        }
    }

}

