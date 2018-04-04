package ru.devsp.app.mtgcollections.view.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.devsp.app.mtgcollections.view.CardItemFragment


/**
 * Адаптер для галереи
 * Created by gen on 09.10.2017.
 */

class CardPagerAdapter(fm: FragmentManager, items: Array<String>?) : FragmentStatePagerAdapter(fm) {

    private var items: Array<String>

    init {
        if (items == null) {
            this.items = emptyArray()
        } else {
            this.items = items
        }
    }

    fun setItems(items: Array<String>) {
        this.items = items
    }

    override fun getItem(position: Int): Fragment {
        return CardItemFragment.getInstance(items[position])
    }

    override fun getCount(): Int {
        return items.size
    }
}
