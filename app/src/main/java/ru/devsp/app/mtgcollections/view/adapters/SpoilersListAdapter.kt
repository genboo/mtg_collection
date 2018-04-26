package ru.devsp.app.mtgcollections.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.view.adapters.holders.SpoilerHolder
import java.util.HashMap


/**
 * Адаптер для списка карт
 * Created by gen on 28.09.2017.
 */

class SpoilersListAdapter(items: List<Card>?) : RecyclerViewAdapter<Card, SpoilerHolder>(items) {

    private val cards = HashMap<String, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpoilerHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_spoiler, parent, false)
        return SpoilerHolder(v)
    }

    override fun onBindViewHolder(holder: SpoilerHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition), cards,
                View.OnClickListener { v -> onItemClick(v, holder.adapterPosition) })
        onReact(holder.adapterPosition)
    }

    fun setCards(cards: List<Card>?) {
        if (cards != null) {
            for (card in cards) {
                this.cards[card.number] = card.count
            }
        }
    }

}