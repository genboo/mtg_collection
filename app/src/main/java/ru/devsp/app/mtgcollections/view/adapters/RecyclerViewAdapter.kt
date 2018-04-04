package ru.devsp.app.mtgcollections.view.adapters

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.View
import ru.devsp.app.mtgcollections.repository.bound.CardsSetBound
import java.util.ArrayList

abstract class RecyclerViewAdapter<T, H : RecyclerView.ViewHolder> internal constructor(items: List<T>?) : RecyclerView.Adapter<H>() {

    internal var onItemClickListener: OnItemClickListener<T>? = null
    private var onReactListener: OnReactListener? = null
    private var items: List<T>

    private val loadedPage = SparseBooleanArray()
    private var positionToReact: Int = CardsSetBound.PAGES_SIZE

    interface OnItemClickListener<in T> {
        fun click(position: Int, item: T, view: View? = null)
    }

    interface OnReactListener {
        fun load(nextPage: Int)
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

    fun setItems(items: List<T>) {
        this.items = items
    }

    fun getItem(position: Int): T {
        return items[position]
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        onItemClickListener = listener
    }

    fun setOnReactListener(listener: OnReactListener) {
        onReactListener = listener
    }

    fun setPositionToReact(position: Int) {
        positionToReact = position
    }

    internal fun onItemClick(view: View, position: Int, imageView: View? = null) {
        if (position != RecyclerView.NO_POSITION) {
            view.postDelayed({ onItemClickListener?.click(position, items[position], imageView) }, DEFAULT_CLICK_DELAY)
        }
    }

    internal fun onReact(position: Int) {
        if (position != RecyclerView.NO_POSITION
                && !loadedPage.get(position / positionToReact + 1)
                && position % positionToReact == 8) {
            loadedPage.put(position / positionToReact + 1, true)
            onReactListener?.load(position / positionToReact + 1)
        }
    }

    companion object {
        private const val DEFAULT_CLICK_DELAY: Long = 100
    }

}
