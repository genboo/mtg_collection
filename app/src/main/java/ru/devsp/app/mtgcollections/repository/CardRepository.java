package ru.devsp.app.mtgcollections.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.app.mtgcollections.model.api.CardApi;
import ru.devsp.app.mtgcollections.model.api.tools.Resource;
import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.repository.bound.CardBound;
import ru.devsp.app.mtgcollections.repository.bound.CardLocalBound;
import ru.devsp.app.mtgcollections.repository.bound.CardNameBound;
import ru.devsp.app.mtgcollections.repository.bound.CardsSetBound;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Репозиторий данных по карте
 * Created by gen on 31.08.2017.
 */
@Singleton
public class CardRepository {

    private final AppExecutors mAppExecutors;
    private CardApi mCardApi;

    @Inject
    CardRepository(AppExecutors appExecutors, CardApi cardApi) {
        mCardApi = cardApi;
        mAppExecutors = appExecutors;
    }

    /**
     * Получение карты
     *
     * @param set    Сэт
     * @param number Номер в сэте
     * @return
     */
    public LiveData<Resource<List<Card>>> getCard(String set, String number) {
        CardBound bound = new CardBound(mAppExecutors, mCardApi)
                .setParams(set, number);
        bound.create();
        return bound.asLiveData();
    }

    /**
     * Получение карты по названию
     *
     * @param set  Сэт
     * @param name Название
     * @return
     */
    public LiveData<Resource<List<Card>>> getCardByName(String set, String name) {
        CardNameBound bound = new CardNameBound(mAppExecutors, mCardApi)
                .setParams(set, name);
        bound.create();
        return bound.asLiveData();
    }

    /**
     * Получение карт по сету
     *
     * @param set Сэт
     * @return
     */
    public LiveData<Resource<List<Card>>> getCardsBySet(String set, int page) {
        CardsSetBound bound = new CardsSetBound(mAppExecutors, mCardApi)
                .setParams(set, page);
        bound.create();
        return bound.asLiveData();
    }

    /**
     * Получение карты
     *
     * @param id multiverse id
     * @return
     */
    public LiveData<Resource<List<Card>>> getCard(String id) {
        CardLocalBound bound = new CardLocalBound(mAppExecutors, mCardApi)
                .setParams(id);
        bound.create();
        return bound.asLiveData();
    }
}
