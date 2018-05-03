package ru.devsp.app.mtgcollections.repository


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import javax.inject.Inject
import javax.inject.Singleton

import ru.devsp.app.mtgcollections.model.db.CardDao
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.CardColorState
import ru.devsp.app.mtgcollections.model.objects.CardExists
import ru.devsp.app.mtgcollections.model.objects.CardForLibrary
import ru.devsp.app.mtgcollections.model.objects.CardLibraryInfo
import ru.devsp.app.mtgcollections.model.objects.CardManaState
import ru.devsp.app.mtgcollections.model.objects.Filter
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Обработка сохраненных карт
 * Created by gen on 03.10.2017.
 */
@Singleton
class CardLocalRepository @Inject
internal constructor(private val appExecutors: AppExecutors, private val dao: CardDao) {

    /**
     * Все карты
     *
     * @return ld
     */
    val allCards: LiveData<List<Card>>
        get() = dao.allCards

    /**
     * Список желаемого
     * @return ld
     */
    val wishList: LiveData<List<Card>>
        get() = dao.wishList

    private val rarities: Array<String>
        get() {
            return arrayOf(
                    "Basic Land",
                    "Common",
                    "Uncommon",
                    "Rare",
                    "Mythic Rare",
                    "Special"
            )
        }

    private val types: Array<String>
        get() {
            val list = dao.types
            return Array(list.size, { i -> list[i].type })
        }

    private val subtypes: Array<String>
        get() {
            val subtypes = dao.subtypes
            return Array(subtypes.size, { i -> subtypes[i].subtype })
        }

    /**
     * Список сетов
     * @return Список сетов
     */
    private val setNames: Array<String>
        get() {
            val setNames = dao.setNames
            return Array(setNames.size, { i -> setNames[i].setName })
        }

    /**
     * Список цветов
     * @return Список цветов
     */
    private val colors: Array<String>
        get() {
            val colors = dao.colors
            return Array(colors.size, { i -> colors[i].color })
        }

    val filter: LiveData<Filter>
        get() {
            val filter = MutableLiveData<Filter>()
            getFilterParams(filter)
            return filter
        }

    fun save(card: Card) {
        appExecutors.diskIO().execute { dao.insert(card) }
    }

    fun update(card: Card) {
        appExecutors.diskIO().execute { dao.update(card) }
    }

    fun updateLink(parent: String, link: String) {
        appExecutors.diskIO().execute { dao.updateLink(parent, link) }
    }

    fun setChild(card: String) {
        appExecutors.diskIO().execute { dao.updateChildState(card, true) }
    }

    fun getFilteredCard(filter: Filter): LiveData<List<Card>> {
        return dao.getFilteredCards(filter.types, filter.subtypes, filter.colors,
                filter.rarities, filter.sets)
    }

    /**
     * Получение карт по колоде
     *
     * @param library library
     * @return ld
     */
    fun getCardsByLibrary(library: Long): LiveData<List<CardForLibrary>> {
        return dao.getCardsByLibrary(library)
    }

    fun getCard(id: String): LiveData<Card> {
        return dao.getCard(id)
    }

    fun getCardExists(id: String): LiveData<CardExists> {
        return dao.getCardExists(id)
    }

    fun getCardsBySet(set: String): LiveData<List<Card>> {
        return dao.getCardsBySet(set)
    }

    /**
     * Получение колод, в которые входит карта
     *
     * @param id id
     * @return ld
     */
    fun getLibrariesByCard(id: String): LiveData<List<CardLibraryInfo>> {
        return dao.getLibrariesByCard(id)
    }

    fun getLibraryManaState(library: Long): LiveData<List<CardManaState>> {
        return dao.getLibraryManaState(library)
    }

    fun getLibraryColorState(library: Long): LiveData<List<CardColorState>> {
        return dao.getLibraryColorState(library)
    }

    private fun getFilterParams(filterLiveData: MutableLiveData<Filter>) {
        appExecutors.diskIO().execute {
            val filter = Filter()

            filter.colors = colors
            filter.subtypes = subtypes
            filter.rarities = rarities
            filter.sets = setNames
            filter.types = types

            filterLiveData.postValue(filter)
        }
    }

}
