package ru.devsp.app.mtgcollections.repository.bound


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import java.util.HashMap

import ru.devsp.app.mtgcollections.model.api.CardApi
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.tools.ApiResponse
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Получение и кэширование карт по id
 * Created by gen on 14.09.2017.
 */

class CardLocalBound(appExecutors: AppExecutors, private val mCardApi: CardApi) : NetworkBound<List<Card>, List<Card>>(appExecutors) {
    private var lid: String = ""

    fun setParams(id: String): CardLocalBound {
        lid = id
        return this
    }

    override fun saveCallResult(data: List<Card>?) {
        if (data != null && !data.isEmpty()) {
            if (cache.size == MAX_CACHE_SIZE) {
                cache.clear()
            }
            cache[lid] = data
        }
    }

    override fun shouldFetch(data: List<Card>?): Boolean {
        return !cache.containsKey(lid)
    }

    override fun loadSaved(): LiveData<List<Card>> {
        val card = MutableLiveData<List<Card>>()
        if (cache.containsKey(lid)) {
            card.setValue(cache[lid])
        } else {
            card.setValue(null)
        }
        return card
    }

    override fun createCall(): LiveData<ApiResponse<List<Card>>> {
        return mCardApi.getCard(lid)
    }

    companion object {

        private const val MAX_CACHE_SIZE = 100

        private val cache = HashMap<String, List<Card>>()
    }

}