package ru.devsp.app.mtgcollections.view.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_set.view.*
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter

class SetHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Set, listener: RecyclerViewAdapter.OnItemClickListener<Set>?) = with(itemView) {
        setName.text = item.name
        setIcon.setImageDrawable(setIcon.context.getDrawable(Card.getSetIcon(item.code)))
        itemBlock.setOnClickListener({ _ ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener?.click(adapterPosition, item)
            }
        })
    }

}