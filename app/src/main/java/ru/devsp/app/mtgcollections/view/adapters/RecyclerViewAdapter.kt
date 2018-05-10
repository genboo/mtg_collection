package ru.devsp.app.mtgcollections.view.adapters

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.View
import java.util.ArrayList

abstract class RecyclerViewAdapter<T, H : RecyclerView.ViewHolder> internal constructor(items: List<T>?) : RecyclerView.Adapter<H>() {

    private var onItemClickListener: OnItemClickListener<T>? = null
    private var items: List<T>

    private val loadedPage = SparseBooleanArray()

    interface OnItemClickListener<in T> {
        fun click(position: Int, item: T, view: View? = null)
    }

    init {
        if (items == null) {
            this.items = ArrayList()
        } else {
            this.items = items
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getSize() : Int{
        return items.size
    }

    fun setItems(items: List<T>) {
        this.items = items
    }

    fun getItem(position: Int): T {
        return items[position]
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        onItemClickListener = listener
    }

    internal fun onItemClick(view: View, position: Int, imageView: View? = null) {
        if (position != RecyclerView.NO_POSITION) {
            view.postDelayed({ onItemClickListener?.click(position, items[position], imageView) }, DEFAULT_CLICK_DELAY)
        }
    }

    companion object {
        private const val DEFAULT_CLICK_DELAY: Long = 100
    }

}
