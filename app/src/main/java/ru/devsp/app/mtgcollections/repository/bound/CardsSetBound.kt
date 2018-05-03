package ru.devsp.app.mtgcollections.repository.bound


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import java.util.HashMap

import ru.devsp.app.mtgcollections.model.api.CardApi
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.tools.ApiResponse
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Получение и кэширование карт
 * Created by gen on 22.12.2017.
 */

class CardsSetBound(appExecutors: AppExecutors, private val cardApi: CardApi) : NetworkBound<List<Card>, List<Card>>(appExecutors) {

    private var set: String = ""
    private var page: Int = 0

    fun setParams(set: String, page: Int): CardsSetBound {
        this.set = set
        this.page = page
        return this
    }

    override fun saveCallResult(data: List<Card>?) {
        if (data != null && data.isNotEmpty()) {
            if (cache.size == MAX_CACHE_SIZE && page == 1) {
                cache.clear()
                downloadPages.clear()
            }
            if (cache.containsKey(set)) {
                cache[set] = cache[set]?.plus(data)
            } else {
                cache[set] = data
            }
            downloadPages[set + page] = true
        }
    }

    override fun shouldFetch(data: List<Card>?): Boolean {
        return !downloadPages.containsKey(set + page)
    }

    override fun loadSaved(): LiveData<List<Card>> {
        val card = MutableLiveData<List<Card>>()
        if (cache.containsKey(set)) {
            card.setValue(cache[set])
        } else {
            card.setValue(null)
        }
        return card
    }

    override fun createCall(): LiveData<ApiResponse<List<Card>>> {
        return cardApi.getCardsBySet(set, page, PAGES_SIZE)
    }

    companion object {
        const val PAGES_SIZE = 40
        private const val MAX_CACHE_SIZE = 5
        private val cache = mutableMapOf<String, List<Card>?>()
        private val downloadPages = HashMap<String, Boolean>()
    }

}