package ru.devsp.app.mtgcollections.repository


import android.arch.lifecycle.LiveData

import javax.inject.Inject
import javax.inject.Singleton

import ru.devsp.app.mtgcollections.model.api.SetsApi
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
internal constructor(appExecutors: AppExecutors, setsApi: SetsApi) {

    private val bound: SetsBound = SetsBound(appExecutors, setsApi)

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