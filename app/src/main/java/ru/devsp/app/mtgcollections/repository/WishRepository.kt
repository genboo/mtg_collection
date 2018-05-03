package ru.devsp.app.mtgcollections.repository


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import javax.inject.Inject
import javax.inject.Singleton

import ru.devsp.app.mtgcollections.model.db.WishCardDao
import ru.devsp.app.mtgcollections.model.objects.Wish
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Репозиторий данных по списку желаний
 * Created by gen on 21.12.2017.
 */
@Singleton
class WishRepository @Inject
internal constructor(private val appExecutors: AppExecutors, private val wishDao: WishCardDao) {

    fun save(item: Wish): LiveData<Long> {
        val result = MutableLiveData<Long>()
        appExecutors.diskIO().execute {
            val id = wishDao.insert(item)
            appExecutors.mainThread().execute { result.postValue(id) }
        }
        return result
    }

    fun delete(item: Wish) {
        appExecutors.diskIO().execute { wishDao.delete(item) }
    }

}