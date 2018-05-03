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
 * Created by gen on 14.09.2017.
 */

class CardNameBound(appExecutors: AppExecutors, private val mCardApi: CardApi) : NetworkBound<List<Card>, List<Card>>(appExecutors) {
    private var set: String? = null
    private var name: String? = null

    fun setParams(set: String, name: String): CardNameBound {
        this.set = set
        this.name = name
        return this
    }

    override fun saveCallResult(data: List<Card>?) {
        if (data != null && !data.isEmpty()) {
            if (cache.size == MAX_CACHE_SIZE) {
                cache.clear()
            }
            cache[set + name] = data
        }
    }

    override fun shouldFetch(data: List<Card>?): Boolean {
        return !cache.containsKey(set!! + name!!)
    }

    override fun loadSaved(): LiveData<List<Card>> {
        val card = MutableLiveData<List<Card>>()
        if (cache.containsKey(set!! + name!!)) {
            card.setValue(cache[set!! + name!!])
        } else {
            card.setValue(null)
        }
        return card
    }

    override fun createCall(): LiveData<ApiResponse<List<Card>>> {
        return mCardApi.getCardByName(set, name)
    }

    companion object {
        private const val MAX_CACHE_SIZE = 100
        private val cache = HashMap<String, List<Card>>()
    }

}