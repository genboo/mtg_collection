package ru.devsp.app.mtgcollections.view.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class RecyclerViewScrollListener(private val load : (Int) -> Unit, private val pageSize : Int) : RecyclerView.OnScrollListener() {

    private var loading = false
    private var previousTotal = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = recyclerView.layoutManager.childCount
        val totalItemCount = recyclerView.layoutManager.itemCount
        val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        if(previousTotal == 0){
            loading = true
        }

        if (loading && totalItemCount > previousTotal) {
            loading = false
            previousTotal = totalItemCount
        }

        if (!loading && totalItemCount - (visibleItemCount + firstVisibleItem) < DEFAULT_REACT) {
            loading = true
            load(totalItemCount / pageSize)
        }

    }

    companion object {
        const val DEFAULT_REACT = 20
    }
}

