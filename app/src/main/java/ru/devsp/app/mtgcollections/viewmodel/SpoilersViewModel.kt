package ru.devsp.app.mtgcollections.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.tools.Resource
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.repository.CardLocalRepository
import ru.devsp.app.mtgcollections.repository.CardRepository
import ru.devsp.app.mtgcollections.tools.AbsentLiveData

/**
 * Спойлеры по сету
 * Created by gen on 22.12.2017.
 */

class SpoilersViewModel @Inject
internal constructor(private val cardRepository: CardRepository, private val cardLocalRepository: CardLocalRepository) : ViewModel() {

    val cards: LiveData<Resource<List<Card>>>
    val cardsBySet: LiveData<List<Card>>
    private val switcher = MutableLiveData<Params>()

    init {
        cards = Transformations.switchMap(switcher) { params ->
            if (params == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Card>>>()
            }
            cardRepository.getCardsBySet(params.set, params.page)
        }
        cardsBySet = Transformations.switchMap(switcher) { params ->
            if (params == null) {
                return@switchMap AbsentLiveData.create<List<Card>>()
            }
            cardLocalRepository.getCardsBySet(params.set)
        }

    }

    fun setParams(set: String, page: Int) {
        switcher.postValue(Params(set, page))
    }

    private inner class Params internal constructor(internal val set: String, internal val page: Int)

}
