package ru.devsp.app.mtgcollections.repository


import android.arch.lifecycle.LiveData

import javax.inject.Inject
import javax.inject.Singleton

import ru.devsp.app.mtgcollections.model.db.LibraryDao
import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryInfo
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Обработка колод
 * Created by gen on 03.10.2017.
 */
@Singleton
class LibrariesRepository @Inject
constructor(private val appExecutors: AppExecutors, private val dao: LibraryDao) {


    val allLibraries: LiveData<List<Library>>
        get() = dao.librariesList

    val librariesInfoList: LiveData<List<LibraryInfo>>
        get() = dao.librariesInfoList

    fun save(item: Library) {
        appExecutors.diskIO().execute { dao.insert(item) }
    }

    fun update(item: Library) {
        appExecutors.diskIO().execute { dao.update(item) }
    }

}
