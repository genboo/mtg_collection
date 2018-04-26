package ru.devsp.app.mtgcollections.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.LibraryInfo
import ru.devsp.app.mtgcollections.view.adapters.holders.LibraryHolder

/**
 * Адаптер для списка колод
 * Created by gen on 28.09.2017.
 */

class LibrariesListAdapter(items: List<LibraryInfo>?) : RecyclerViewAdapter<LibraryInfo, LibraryHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_library, parent, false)
        return LibraryHolder(v)
    }

    override fun onBindViewHolder(holder: LibraryHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition),
                View.OnClickListener { v -> onItemClick(v, holder.adapterPosition) })
    }

}
