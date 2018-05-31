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
    private var loading = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpoilerHolder =
            when (viewType) {
                TYPE_LOADING -> SpoilerHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_loading, parent, false))
                else -> SpoilerHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_spoiler, parent, false))
            }

    override fun onBindViewHolder(holder: SpoilerHolder, position: Int) {
        if (getItemViewType(position) == TYPE_MAIN) {
            holder.bind(getItem(holder.adapterPosition), cards,
                    View.OnClickListener { v -> onItemClick(v, holder.adapterPosition) })
        }else{
            holder.switchLoading(loading)
        }

    }

    fun setCards(cards: List<Card>?) {
        if (cards != null) {
            for (card in cards) {
                this.cards[card.number] = card.count
            }
        }
    }

    fun getCardCount(number:String):Int{
        return this.cards[number] ?: 0
    }

    fun setLoading(loading : Boolean){
        this.loading = loading
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < getSize()) TYPE_MAIN else TYPE_LOADING
    }

    companion object {
        const val TYPE_MAIN = 1
        const val TYPE_LOADING = 2
    }

}