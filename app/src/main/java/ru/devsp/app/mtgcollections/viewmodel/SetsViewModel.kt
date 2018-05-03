package ru.devsp.app.mtgcollections.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.tools.Resource
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.repository.SetsRepository

/**
 * Список сетов
 * Created by gen on 22.12.2017.
 */

class SetsViewModel @Inject
internal constructor(private val setsRepository: SetsRepository) : ViewModel() {
    val sets: LiveData<Resource<List<Set>>>
        get() {
            return setsRepository.sets
        }

}
