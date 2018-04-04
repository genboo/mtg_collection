package ru.devsp.app.mtgcollections.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.view.adapters.holders.WishHolder


/**
 * Адаптер для списка хотелок
 * Created by gen on 23.12.2017.
 */

class WishListAdapter(items: List<Card>?) : RecyclerViewAdapter<Card, WishHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_spoiler, parent, false)
        return WishHolder(v)
    }

    override fun onBindViewHolder(holder: WishHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
        holder.itemBlock.setOnClickListener({ _ ->
            onItemClick(holder.itemBlock, holder.adapterPosition, holder.cardImage)
        })
    }

}
