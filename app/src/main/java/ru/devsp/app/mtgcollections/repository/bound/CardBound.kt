package ru.devsp.app.mtgcollections.repository.bound


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import ru.devsp.app.mtgcollections.model.api.CardApi
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.tools.ApiResponse
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Получение и кэширование карт
 * Created by gen on 14.09.2017.
 */

class CardBound(appExecutors: AppExecutors, private val cardApi: CardApi) : NetworkBound<List<Card>, List<Card>>(appExecutors) {
    private var set: String = ""
    private var number: String = ""

    fun setParams(set: String, number: String): CardBound {
        this.set = set
        this.number = number
        return this
    }

    override fun saveCallResult(data: List<Card>?) {
        if (data != null && !data.isEmpty()) {
            if (cache.size == MAX_CACHE_SIZE) {
                cache.clear()
            }
            cache[set + number] = data[0]
        }
    }

    override fun shouldFetch(data: List<Card>?): Boolean {
        return !cache.containsKey(set + number)
    }

    override fun loadSaved(): LiveData<List<Card>> {
        val data = MutableLiveData<List<Card>>()
        val card = cache[set + number]
        if (card != null) {
            data.setValue(arrayListOf(card))
        } else {
            data.setValue(null)
        }
        return data
    }

    override fun createCall(): LiveData<ApiResponse<List<Card>>> {
        return cardApi.getCard(set, number)
    }

    companion object {
        private const val MAX_CACHE_SIZE = 100
        private val cache = HashMap<String, Card>()
    }

}