package ru.devsp.app.mtgcollections.repository


import android.arch.lifecycle.LiveData

import javax.inject.Inject
import javax.inject.Singleton

import ru.devsp.app.mtgcollections.model.api.CardApi
import ru.devsp.app.mtgcollections.model.tools.Resource
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.repository.bound.CardBound
import ru.devsp.app.mtgcollections.repository.bound.CardLocalBound
import ru.devsp.app.mtgcollections.repository.bound.CardNameBound
import ru.devsp.app.mtgcollections.repository.bound.CardsSetBound
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Репозиторий данных по карте
 * Created by gen on 31.08.2017.
 */
@Singleton
class CardRepository @Inject
internal constructor(private val appExecutors: AppExecutors, private val cardApi: CardApi) {

    /**
     * Получение карты
     *
     * @param set    Сэт
     * @param number Номер в сэте
     * @return
     */
    fun getCard(set: String, number: String): LiveData<Resource<List<Card>>> {
        val bound = CardBound(appExecutors, cardApi)
                .setParams(set, number)
        bound.create()
        return bound.asLiveData()
    }

    /**
     * Получение карты по названию
     *
     * @param set  Сэт
     * @param name Название
     * @return
     */
    fun getCardByName(set: String, name: String): LiveData<Resource<List<Card>>> {
        val bound = CardNameBound(appExecutors, cardApi)
                .setParams(set, name)
        bound.create()
        return bound.asLiveData()
    }

    /**
     * Получение карт по сету
     *
     * @param set Сэт
     * @return
     */
    fun getCardsBySet(set: String, page: Int): LiveData<Resource<List<Card>>> {
        val bound = CardsSetBound(appExecutors, cardApi)
                .setParams(set, page)
        bound.create()
        return bound.asLiveData()
    }

    /**
     * Получение карты
     *
     * @param id multiverse id
     * @return
     */
    fun getCard(id: String): LiveData<Resource<List<Card>>> {
        val bound = CardLocalBound(appExecutors, cardApi)
                .setParams(id)
        bound.create()
        return bound.asLiveData()
    }
}
