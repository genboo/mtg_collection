package ru.devsp.app.mtgcollections.repository.bound


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import java.util.ArrayList

import ru.devsp.app.mtgcollections.model.api.SetsApi
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.model.tools.ApiResponse
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Получение и кэширование сетов
 * Created by gen on 14.09.2017.
 */

class SetsBound(appExecutors: AppExecutors, private val setsApi: SetsApi) : NetworkBound<List<Set>, List<Set>>(appExecutors) {

    private val cache = ArrayList<Set>()

    override fun saveCallResult(data: List<Set>?) {
        if (data != null && !data.isEmpty()) {
            cache.clear()
            cache.addAll(data)
        }
    }

    override fun shouldFetch(data: List<Set>?): Boolean {
        return cache.isEmpty()
    }

    override fun loadSaved(): LiveData<List<Set>> {
        val sets = MutableLiveData<List<Set>>()
        if (cache.isEmpty()) {
            sets.setValue(null)
        } else {
            sets.setValue(cache)
        }
        return sets
    }

    override fun createCall(): LiveData<ApiResponse<List<Set>>> {
        return setsApi.sets
    }

}