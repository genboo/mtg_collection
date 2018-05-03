package ru.devsp.app.mtgcollections.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryInfo
import ru.devsp.app.mtgcollections.repository.LibrariesRepository

/**
 *
 * Created by gen on 31.08.2017.
 */

class LibrariesViewModel @Inject
internal constructor(private val librariesRepository: LibrariesRepository) : ViewModel() {
    val allLibraries: LiveData<List<LibraryInfo>> = librariesRepository.librariesInfoList

    fun add(item: Library) {
        librariesRepository.save(item)
    }

}
