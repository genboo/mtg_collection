package ru.devsp.app.mtgcollections.view.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_library.view.*
import ru.devsp.app.mtgcollections.model.objects.LibraryInfo
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter

class LibraryHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: LibraryInfo, listener: RecyclerViewAdapter.OnItemClickListener<LibraryInfo>?) = with(itemView) {
        libraryName.text = item.name
        libraryCardsCount.text = String.format("Карт в колоде: %s", item.cardsCount)

        itemBlock.setOnClickListener({ _ ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener?.click(adapterPosition, item)
            }
        })
    }

}