package ru.devsp.app.mtgcollections.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.tools.Resource
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.CardLibraryInfo
import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryCard
import ru.devsp.app.mtgcollections.repository.CardLocalRepository
import ru.devsp.app.mtgcollections.repository.CardRepository
import ru.devsp.app.mtgcollections.repository.LibrariesRepository
import ru.devsp.app.mtgcollections.repository.LibraryCardRepository
import ru.devsp.app.mtgcollections.tools.AbsentLiveData

/**
 * Управление карточкой карты
 * Created by gen on 31.08.2017.
 */

class CardViewModel @Inject
internal constructor(private val cardLocalRepository: CardLocalRepository,
                     private val libraryCardRepository: LibraryCardRepository,
                     librariesRepository: LibrariesRepository,
                     private val cardRepository: CardRepository) : ViewModel() {

    /**
     * Выбранная карта
     * @return Выбранная карта
     */
    val card: LiveData<Card>
    /**
     * Обратная сторона
     * @return Обратная сторона
     */
    val cardSide: LiveData<Card>
    /**
     * Карта из сети (для обновления информации)
     * @return Карта из сети
     */
    val cardNetwork: LiveData<Resource<List<Card>>>

    /**
     * Все колоды, в которые входит карта
     * @return Все колоды, в которые входит карта
     */
    val librariesByCard: LiveData<List<CardLibraryInfo>>
    val libraries: LiveData<List<Library>>
    private val switcher = MutableLiveData<String>()
    private val switcherNetwork = MutableLiveData<String>()
    private val switcherChild = MutableLiveData<String>()


    init {

        card = Transformations.switchMap(switcher) { id ->
            if (id == null) {
                return@switchMap AbsentLiveData . create < Card >()
            } else {
                return@switchMap cardLocalRepository . getCard (id)
            }
        }
        cardSide = Transformations.switchMap(switcherChild) { id ->
            if (id == null) {
                return@switchMap AbsentLiveData . create < Card >()
            } else {
                return@switchMap cardLocalRepository . getCard (id)
            }
        }
        librariesByCard = Transformations.switchMap(switcher) { id ->
            if (id == null) {
                return@switchMap AbsentLiveData . create < List < CardLibraryInfo > > ()
            } else {
                return@switchMap cardLocalRepository . getLibrariesByCard (id)
            }
        }

        cardNetwork = Transformations.switchMap(switcherNetwork) { id ->
            if (id == null) {
                return@switchMap AbsentLiveData . create < Resource < List < Card > > >()
            }
            cardRepository.getCard(id)
        }

        libraries = librariesRepository.allLibraries

    }

    fun setId(id: String) {
        switcher.value = id
    }

    fun setIdNetwork(id: String) {
        switcherNetwork.value = id
    }

    fun setIdChild(id: String) {
        switcherChild.value = id
    }

    /**
     * Сохранение количества карт в колоде
     * @param item Карта
     */
    fun saveCount(item: LibraryCard) {
        if (item.count > 0) {
            libraryCardRepository.update(item)
        } else {
            libraryCardRepository.delete(item)
        }
    }

    /**
     * Обновление информации по карте
     * @param item карта
     */
    fun updateCard(item: Card) {
        cardLocalRepository.update(item)
    }

    /**
     * Обновление связи карт
     * @param parent родитель
     * @param link ссылка
     */
    fun updateLink(parent: String, link: String) {
        cardLocalRepository.updateLink(parent, link)
        cardLocalRepository.setChild(link)
    }

    fun addToLibrary(item: LibraryCard) {
        libraryCardRepository.save(item)
    }
}
