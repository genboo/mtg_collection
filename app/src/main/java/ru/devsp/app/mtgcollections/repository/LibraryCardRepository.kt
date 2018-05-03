package ru.devsp.app.mtgcollections.repository


import javax.inject.Inject
import javax.inject.Singleton

import ru.devsp.app.mtgcollections.model.db.LibraryCardDao
import ru.devsp.app.mtgcollections.model.objects.LibraryCard
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Обработка связей карт и колод
 * Created by gen on 03.10.2017.
 */
@Singleton
class LibraryCardRepository @Inject
internal constructor(private val appExecutors: AppExecutors, private val dao: LibraryCardDao) {

    fun save(item: LibraryCard) {
        appExecutors.diskIO().execute { dao.insert(item) }
    }

    fun update(item: LibraryCard) {
        appExecutors.diskIO().execute { dao.update(item) }
    }

    fun delete(item: LibraryCard) {
        appExecutors.diskIO().execute { dao.delete(item) }
    }
}
