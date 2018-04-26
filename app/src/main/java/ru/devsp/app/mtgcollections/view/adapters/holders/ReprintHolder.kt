package ru.devsp.app.mtgcollections.view.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_reprint.view.*
import ru.devsp.app.mtgcollections.model.objects.Card

class ReprintHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: String, listener: View.OnClickListener) = with(itemView) {
        setIcon.setImageDrawable(setIcon.context.resources
                .getDrawable(Card.getSetIcon(item), setIcon.context.theme))
        setName.text = item
        itemBlock.setOnClickListener(listener)
    }

}