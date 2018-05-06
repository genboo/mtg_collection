package ru.devsp.app.mtgcollections.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.Filter
import ru.devsp.app.mtgcollections.model.objects.SetName
import ru.devsp.app.mtgcollections.repository.CardLocalRepository
import ru.devsp.app.mtgcollections.tools.AbsentLiveData

/**
 * Коллекция карт
 * Created by gen on 31.08.2017.
 */

class WishViewModel @Inject
internal constructor(cardLocalRepository: CardLocalRepository) : ViewModel() {

    private val filterSwitcher = MutableLiveData<Filter>()
    val wishList: LiveData<List<Card>>
    val wishSetNames: LiveData<List<SetName>> = cardLocalRepository.wishSetNamesList

    val selectedFilter: Filter?
        get() = filterSwitcher.value

    init {
        wishList = Transformations.switchMap(filterSwitcher) { filter ->
            when (filter) {
                null -> AbsentLiveData.create()
                else ->
                    if (filter.full)
                        cardLocalRepository.wishList
                    else
                        cardLocalRepository.getFilteredWishList(filter.sets)
            }
        }
    }

    fun setFilter(filter: Filter) {
        if(filter.sets.isEmpty()){
            filter.full = true
        }
        filterSwitcher.postValue(filter)
    }
}
