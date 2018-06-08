package ru.devsp.app.mtgcollections.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.view.adapters.holders.ReprintHolder

/**
 * Адаптер для списка репринтов
 * Created by gen on 18.12.2017.
 */

class ReprintListAdapter(items: List<String>?) : RecyclerViewAdapter<String, ReprintHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReprintHolder =
            when (viewType) {
                TYPE_OFFSET -> ReprintHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_offset, parent, false))
                else -> ReprintHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_reprint, parent, false))
            }


    override fun getItemCount(): Int {
        return super.getItemCount() + 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 || position == itemCount - 1)
            TYPE_OFFSET
        else
            TYPE_MAIN
    }

    override fun onBindViewHolder(holder: ReprintHolder, position: Int) {
        if (getItemViewType(holder.adapterPosition) == TYPE_MAIN) {
            holder.bind(getItem(position - 1),
                    View.OnClickListener { v -> onItemClick(v, holder.adapterPosition - 1) })
        }
    }

    override fun setItems(items: List<String>) {
        super.setItems(items.reversed())
    }

    companion object {
        private const val TYPE_OFFSET = 1
        private const val TYPE_MAIN = 2
    }

}