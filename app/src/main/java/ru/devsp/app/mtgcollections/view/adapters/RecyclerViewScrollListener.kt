package ru.devsp.app.mtgcollections.view.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class RecyclerViewScrollListener(val listener: OnReactListener, private val pageSize : Int) : RecyclerView.OnScrollListener() {

    private var loading = false
    private var previousTotal = 0

    interface OnReactListener {
        fun load(nextPage: Int)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val visibleItemCount = recyclerView.layoutManager.childCount
        val totalItemCount = recyclerView.layoutManager.itemCount
        val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        if (loading && totalItemCount > previousTotal) {
            loading = false
            previousTotal = totalItemCount
        }

        if (!loading && totalItemCount - (visibleItemCount + firstVisibleItem) < DEFAULT_REACT) {
            loading = true
            listener.load(totalItemCount / pageSize)
        }

    }

    companion object {
        const val DEFAULT_REACT = 20
    }
}

