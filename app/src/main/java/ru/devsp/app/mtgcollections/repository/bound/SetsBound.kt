package ru.devsp.app.mtgcollections.repository.bound


import android.arch.lifecycle.LiveData
import ru.devsp.app.mtgcollections.model.api.SetsApi
import ru.devsp.app.mtgcollections.model.db.SetsDao
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.model.tools.ApiResponse
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Получение и кэширование сетов
 * Created by gen on 14.09.2017.
 */

class SetsBound(appExecutors: AppExecutors, private val setsDao: SetsDao, private val setsApi: SetsApi) :
        NetworkBound<List<Set>, List<Set>>(appExecutors) {

    private var needLoad = true

    override fun saveCallResult(data: List<Set>?) {
        if (data != null && !data.isEmpty()) {
            setsDao.clear()
            setsDao.insert(data)
            needLoad = false
        }
    }

    override fun shouldFetch(data: List<Set>?): Boolean {
        return needLoad
    }

    override fun loadSaved(): LiveData<List<Set>> {
        return setsDao.getSets()
    }

    override fun createCall(): LiveData<ApiResponse<List<Set>>> {
        return setsApi.sets
    }

}