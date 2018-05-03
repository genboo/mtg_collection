package ru.devsp.app.mtgcollections.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.tools.Resource
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.CardExists
import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryCard
import ru.devsp.app.mtgcollections.model.objects.Wish
import ru.devsp.app.mtgcollections.repository.AdditionalInfoCardRepository
import ru.devsp.app.mtgcollections.repository.CardLocalRepository
import ru.devsp.app.mtgcollections.repository.CardRepository
import ru.devsp.app.mtgcollections.repository.LibrariesRepository
import ru.devsp.app.mtgcollections.repository.LibraryCardRepository
import ru.devsp.app.mtgcollections.repository.WishRepository
import ru.devsp.app.mtgcollections.tools.AbsentLiveData

/**
 * Created by gen on 31.08.2017.
 */

class SearchViewModel @Inject
internal constructor(private val cardRepository: CardRepository, private val cardLocalRepository: CardLocalRepository,
                     librariesRepository: LibrariesRepository, private val libraryCardRepository: LibraryCardRepository,
                     private val additionalInfoCardRepository: AdditionalInfoCardRepository,
                     private val wishRepository: WishRepository) : ViewModel() {

    /**
     * Колоды
     */
    val libraries: LiveData<List<Library>>

    /**
     * Загруженная из сети карта
     */
    /**
     * Загруженая из сети карта
     *
     * @return Карта
     */
    val card: LiveData<Resource<List<Card>>>
    private val searchCardParams = MutableLiveData<SearchCardParams>()

    /**
     * Локальная копия карты
     */
    /**
     * Локальная копия карты
     *
     * @return Локальная карта
     */
    val cardExist: LiveData<CardExists>
    private val cardExistSwitcher = MutableLiveData<String>()

    init {

        card = Transformations.switchMap(searchCardParams) { params ->
            if (params == null) {
                return@switchMap AbsentLiveData.create<Resource<List<Card>>>()
            } else if (params.name != null) {
                return@switchMap cardRepository.getCardByName(params.set, params.name)
            }
            cardRepository.getCard(params.set, params.number ?: "")
        }

        cardExist = Transformations.switchMap(cardExistSwitcher) { id ->
            if (id == null) {
                return@switchMap AbsentLiveData.create<CardExists>()
            }
            cardLocalRepository.getCardExists(id)
        }

        libraries = librariesRepository.allLibraries
    }

    /**
     * Поиск карты
     *
     * @param set    Выпуск
     * @param number Номер в выпуске
     */
    fun search(set: String, number: String?, name: String?) {
        val update = SearchCardParams(set, number, name)
        searchCardParams.value = update
    }

    /**
     * Проверка сохраненной карты
     *
     * @param id Карта
     */
    fun checkCard(id: String) {
        cardExistSwitcher.value = id
    }

    /**
     * Сохранение локальной копии
     *
     * @param card Полученная из сети карта
     */
    fun save(card: Card?) {
        if(card != null) {
            cardLocalRepository.save(card)
            additionalInfoCardRepository.save(card)
        }
    }

    /**
     * Сохранение в списке желаний
     * @param item
     * @return
     */
    fun addToWish(item: Wish): LiveData<Long> {
        return wishRepository.save(item)
    }

    fun deleteWish(item: Wish) {
        wishRepository.delete(item)
    }

    /**
     * Добавление в библиотеку
     *
     * @param item Связь карты и колоды
     */
    fun addToLibrary(item: LibraryCard) {
        libraryCardRepository.save(item)
    }


    private class SearchCardParams internal constructor(internal val set: String, internal val number: String?, internal val name: String?)

}
