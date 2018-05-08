package ru.devsp.app.mtgcollections.repository.bound


import android.arch.lifecycle.LiveData
import android.content.SharedPreferences
import ru.devsp.app.mtgcollections.di.modules.Prefs
import ru.devsp.app.mtgcollections.model.api.SetsApi
import ru.devsp.app.mtgcollections.model.db.SetsDao
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.model.tools.ApiResponse
import ru.devsp.app.mtgcollections.tools.AppExecutors
import java.util.*

/**
 * Получение и кэширование сетов
 * Created by gen on 14.09.2017.
 */

class SetsBound(appExecutors: AppExecutors,
                private val setsDao: SetsDao,
                private val setsApi: SetsApi,
                private val prefs: SharedPreferences) :
        NetworkBound<List<Set>, List<Set>>(appExecutors) {

    override fun saveCallResult(data: List<Set>?) {
        if (data != null && !data.isEmpty()) {
            setsDao.clear()
            setsDao.insert(data)
            val editor = prefs.edit()
            editor.putLong(Prefs.SETS_TIME, Date().time)
            editor.apply()
        }
    }

    override fun shouldFetch(data: List<Set>?): Boolean {
        if (prefs.getLong(Prefs.SETS_TIME, 0) + EXPIRE < Date().time) {
            return true
        }
        return false
    }

    override fun loadSaved(): LiveData<List<Set>> {
        return setsDao.getSets()
    }

    override fun createCall(): LiveData<ApiResponse<List<Set>>> {
        return setsApi.sets
    }

    companion object {
        //15 дней
        private const val EXPIRE = 1000 * 60 * 60 * 24 * 15
    }

}