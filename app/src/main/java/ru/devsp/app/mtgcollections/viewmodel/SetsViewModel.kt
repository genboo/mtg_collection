package ru.devsp.app.mtgcollections.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.content.SharedPreferences
import ru.devsp.app.mtgcollections.di.modules.Prefs

import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.tools.Resource
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.repository.SetsRepository
import ru.devsp.app.mtgcollections.tools.AbsentLiveData

/**
 * Список сетов
 * Created by gen on 22.12.2017.
 */

class SetsViewModel @Inject
internal constructor(private val setsRepository: SetsRepository,
                     private val prefs: SharedPreferences) : ViewModel() {

    private val switcher = MutableLiveData<Boolean>()

    private var sets: LiveData<Resource<List<Set>>>

    init {
        sets = Transformations.switchMap(switcher) { load ->
            if (load) {
                return@switchMap setsRepository.sets
            }
            AbsentLiveData.create<Resource<List<Set>>>()
        }
    }

    fun getSets(): LiveData<Resource<List<Set>>> {
        return sets
    }

    fun loadSets(){
        switcher.postValue(true)
    }

    fun clearExpire() {
        val editor = prefs.edit()
        editor.remove(Prefs.SETS_TIME)
        editor.apply()
    }
}
