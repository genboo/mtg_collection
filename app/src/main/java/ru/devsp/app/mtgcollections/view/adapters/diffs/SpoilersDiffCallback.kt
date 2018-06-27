package ru.devsp.app.mtgcollections.view.adapters.diffs

import android.support.v7.util.DiffUtil
import ru.devsp.app.mtgcollections.model.objects.Card

class SpoilersDiffCallback(private val oldList: List<Card>,
                           private val newList: List<Card>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
                && oldList[oldItemPosition].set == newList[newItemPosition].set
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}