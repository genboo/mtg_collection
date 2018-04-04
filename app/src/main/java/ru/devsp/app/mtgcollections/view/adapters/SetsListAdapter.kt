package ru.devsp.app.mtgcollections.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.view.adapters.holders.SetHolder

/**
 * Адаптер для списка сетов
 * Created by gen on 22.12.2017.
 */

class SetsListAdapter(items: List<Set>?) : RecyclerViewAdapter<Set, SetHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_set, parent, false)
        return SetHolder(v)
    }

    override fun onBindViewHolder(holder: SetHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition), onItemClickListener)
    }

}
