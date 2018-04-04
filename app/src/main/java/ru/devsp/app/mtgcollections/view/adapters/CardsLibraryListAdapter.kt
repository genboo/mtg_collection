package ru.devsp.app.mtgcollections.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.CardForLibrary
import ru.devsp.app.mtgcollections.view.adapters.holders.CardLibraryHolder
import java.util.ArrayList

/**
 * Адаптер для списка карт
 * Created by gen on 28.09.2017.
 */
class CardsLibraryListAdapter(items: List<CardListItem>?) : RecyclerViewAdapter<CardListItem, CardLibraryHolder>(items) {

    fun setCards(cards: List<CardForLibrary>) {
        val items = ArrayList<CardListItem>()
        for (i in cards.indices) {
            if (i == 0 || cards[i - 1].typeSingle != cards[i].typeSingle) {
                items.add(CardListItem(cards[i].typeSingle, null))
            }
            items.add(CardListItem(null, cards[i]))
        }
        super.setItems(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardLibraryHolder = when (viewType) {
        TYPE_HEADER -> CardLibraryHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_card_header, parent, false))
        else -> CardLibraryHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_card, parent, false))
    }

    override fun onBindViewHolder(holder: CardLibraryHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition), onItemClickListener)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).card == null) TYPE_HEADER else TYPE_ITEM
    }

    companion object {
        private const val TYPE_HEADER = 1
        private const val TYPE_ITEM = 2
    }

}