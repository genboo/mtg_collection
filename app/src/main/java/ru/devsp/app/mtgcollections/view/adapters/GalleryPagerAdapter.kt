package ru.devsp.app.mtgcollections.view.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.view.GalleryItemFragment
import java.util.ArrayList

/**
 * Адаптер для галереи
 * Created by gen on 09.10.2017.
 */

class GalleryPagerAdapter(fm: FragmentManager, items: List<Card>?) : FragmentStatePagerAdapter(fm) {

    private var items: List<Card>

    init {
        if (items == null) {
            this.items = ArrayList()
        } else {
            this.items = items
        }
    }

    fun setItems(items: List<Card>) {
        this.items = items
    }

    override fun getItem(position: Int): Fragment {
        return GalleryItemFragment.getInstance(items[position].imageUrl, items[position].id)
    }

    override fun getCount(): Int {
        return items.size
    }
}
