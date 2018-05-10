package ru.devsp.app.mtgcollections.repository.bound


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import com.google.gson.Gson

import ru.devsp.app.mtgcollections.model.api.CardApi
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.tools.ApiResponse
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Получение и кэширование карт
 * Created by gen on 22.12.2017.
 */

class CardsSetBound(appExecutors: AppExecutors, private val prefs: SharedPreferences, private val cardApi: CardApi) : NetworkBound<List<Card>, List<Card>>(appExecutors) {

    private var set: String = ""
    private var page: Int = 0

    fun setParams(set: String, page: Int): CardsSetBound {
        this.set = set
        this.page = page
        return this
    }

    override fun saveCallResult(data: List<Card>?) {
        if (data != null && data.isNotEmpty()) {
            val json = prefs.getString(set, null)
            val list: List<Card> = when(json == null){
                true -> data
                else -> Gson().fromJson<Array<Card>>(json, Array<Card>::class.java).asList().plus(data)
            }
            val editor = prefs.edit()
            editor.putString(set, Gson().toJson(list))
            editor.putInt(set + "_page", page)
            editor.apply()
        }
    }

    override fun shouldFetch(data: List<Card>?): Boolean {
        val savedPage = prefs.getInt(set + "_page", 0)
        return page > savedPage
    }

    override fun loadSaved(): LiveData<List<Card>> {
        val card = MutableLiveData<List<Card>>()
        val json = prefs.getString(set, null)
        if (json == null) {
            card.value = null
        } else {
            card.value = Gson().fromJson<Array<Card>>(json, Array<Card>::class.java).asList()
        }
        return card
    }

    override fun createCall(): LiveData<ApiResponse<List<Card>>> {
        return cardApi.getCardsBySet(set, page, PAGES_SIZE)
    }

    companion object {
        const val PAGES_SIZE = 40
    }

}