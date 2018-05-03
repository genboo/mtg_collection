package ru.devsp.app.mtgcollections.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.CardColorState
import ru.devsp.app.mtgcollections.model.objects.CardForLibrary
import ru.devsp.app.mtgcollections.model.objects.CardManaState
import ru.devsp.app.mtgcollections.model.objects.Filter
import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.repository.CardLocalRepository
import ru.devsp.app.mtgcollections.repository.LibrariesRepository
import ru.devsp.app.mtgcollections.tools.AbsentLiveData

/**
 * Коллекция карт
 * Created by gen on 31.08.2017.
 */

class CollectionViewModel @Inject
internal constructor(private val cardLocalRepository: CardLocalRepository,
                     private val librariesRepository: LibrariesRepository) : ViewModel() {

    /**
     * Отфильтрованные карты
     *
     * @return
     */
    val filteredCards: LiveData<List<Card>>
    /**
     * Список доступных фильтров
     *
     * @return
     */
    val filter: LiveData<Filter>
    /**
     * Все карты заданной колоды
     *
     * @return
     */
    val cardsByLibrary: LiveData<List<CardForLibrary>>
    /**
     * Статистика по мана стоимости карт
     *
     * @return
     */
    val cardsManaState: LiveData<List<CardManaState>>
    /**
     * Статистика по цветам карт
     *
     * @return
     */
    val cardsColorState: LiveData<List<CardColorState>>

    private val switcher = MutableLiveData<Long>()
    private val filterSwitcher = MutableLiveData<Filter>()

    /**
     * Действующие фильтры
     *
     * @return
     */
    val selectedFilter: Filter?
        get() = filterSwitcher.value

    init {

        cardsByLibrary = Transformations.switchMap(switcher) { id ->
            if (id == 0L) {
                return@switchMap AbsentLiveData.create<List<CardForLibrary>>()
            } else {
                return@switchMap cardLocalRepository.getCardsByLibrary(id)
            }
        }

        cardsManaState = Transformations.switchMap(switcher) { id ->
            if (id == 0L) {
                return@switchMap AbsentLiveData.create<List<CardManaState>>()
            } else {
                return@switchMap cardLocalRepository.getLibraryManaState(id)
            }
        }

        cardsColorState = Transformations.switchMap(switcher) { id ->
            if (id == 0L) {
                return@switchMap AbsentLiveData.create<List<CardColorState>>()
            } else {
                return@switchMap cardLocalRepository.getLibraryColorState(id)
            }
        }

        filter = cardLocalRepository.filter

        filteredCards = Transformations.switchMap(filterSwitcher) { filter ->
            when (filter) {
                null -> AbsentLiveData.create()
                else ->
                    if (filter.full)
                        cardLocalRepository.allCards
                    else
                        cardLocalRepository.getFilteredCard(filter)
            }
        }

    }

    fun setFilter(filter: Filter) {
        if (filter.subtypes.isEmpty() && filter.colors.isEmpty()
                && filter.sets.isEmpty() && filter.rarities.isEmpty()
                && filter.types.isEmpty()) {
            filter.full = true
        } else if (this.filter.value != null) {
            if (filter.types.isEmpty()) {
                filter.types = this.filter.value!!.types
            }
            if (filter.subtypes.isEmpty()) {
                filter.subtypes = this.filter.value!!.subtypes
            }
            if (filter.colors.isEmpty()) {
                filter.colors = this.filter.value!!.colors
            }
            if (filter.sets.isEmpty()) {
                filter.sets = this.filter.value!!.sets
            }
            if (filter.rarities.isEmpty()) {
                filter.rarities = this.filter.value!!.rarities
            }
        }
        filterSwitcher.postValue(filter)
    }

    fun setLibrary(id: Long) {
        switcher.value = id
    }

    fun updateLibrary(item: Library) {
        librariesRepository.update(item)
    }


}
