package ru.devsp.app.mtgcollections.repository


import android.arch.lifecycle.LiveData
import android.content.SharedPreferences

import javax.inject.Inject
import javax.inject.Singleton

import ru.devsp.app.mtgcollections.model.api.SetsApi
import ru.devsp.app.mtgcollections.model.db.SetsDao
import ru.devsp.app.mtgcollections.model.tools.Resource
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.repository.bound.SetsBound
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Репозиторий данных по сетам
 * Created by gen on 22.12.2017.
 */
@Singleton
class SetsRepository @Inject
internal constructor(appExecutors: AppExecutors, setsDao: SetsDao, setsApi: SetsApi, prefs: SharedPreferences) {

    private val bound: SetsBound = SetsBound(appExecutors, setsDao, setsApi, prefs)

    /**
     * Получение списка сетов
     *
     * @return sets
     */
    val sets: LiveData<Resource<List<Set>>>
        get() {
            bound.create()
            return bound.asLiveData()
        }

}
