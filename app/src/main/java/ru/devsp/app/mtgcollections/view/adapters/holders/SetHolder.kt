package ru.devsp.app.mtgcollections.view.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_set.view.*
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.Set

class SetHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Set, clickListener: View.OnClickListener) = with(itemView) {
        setName.text = item.name
        setIcon.setImageDrawable(setIcon.context.getDrawable(Card.getSetIcon(item.code)))
        itemBlock.setOnClickListener(clickListener)
    }

}