package ru.devsp.app.mtgcollections.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.view.adapters.holders.CardHolder

class CardsListAdapter(items: List<Card>?) : RecyclerViewAdapter<Card, CardHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_card, parent, false)
        return CardHolder(v)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(getItem(position),
                View.OnClickListener { v -> onItemClick(v, holder.adapterPosition, holder.cardImage) })
    }
}