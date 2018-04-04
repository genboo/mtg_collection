package ru.devsp.app.mtgcollections.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.app.mtgcollections.model.db.CardDao;
import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.model.objects.CardColorState;
import ru.devsp.app.mtgcollections.model.objects.CardExists;
import ru.devsp.app.mtgcollections.model.objects.CardForLibrary;
import ru.devsp.app.mtgcollections.model.objects.CardLibraryInfo;
import ru.devsp.app.mtgcollections.model.objects.CardManaState;
import ru.devsp.app.mtgcollections.model.objects.Color;
import ru.devsp.app.mtgcollections.model.objects.Filter;
import ru.devsp.app.mtgcollections.model.objects.SetName;
import ru.devsp.app.mtgcollections.model.objects.Subtype;
import ru.devsp.app.mtgcollections.model.objects.Type;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Обработка сохраненных карт
 * Created by gen on 03.10.2017.
 */
@Singleton
public class CardLocalRepository {

    private final AppExecutors mAppExecutors;

    private CardDao mDao;

    @Inject
    CardLocalRepository(AppExecutors appExecutors, CardDao dao) {
        mAppExecutors = appExecutors;
        mDao = dao;
    }

    public void save(Card card) {
        mAppExecutors.diskIO().execute(() -> mDao.insert(card));
    }

    public void update(Card card) {
        mAppExecutors.diskIO().execute(() -> mDao.update(card));
    }

    public void updateLink(String parent, String link) {
        mAppExecutors.diskIO().execute(() -> mDao.updateLink(parent, link));
    }

    public void setChild(String card) {
        mAppExecutors.diskIO().execute(() -> mDao.updateChildState(card, true));
    }

    public LiveData<List<Card>> getFilteredCard(Filter filter) {
        return mDao.getFilteredCards(filter.types, filter.subtypes, filter.colors,
                filter.rarities, filter.sets);
    }

    /**
     * Все карты
     *
     * @return ld
     */
    public LiveData<List<Card>> getAllCards() {
        return mDao.getAllCards();
    }

    /**
     * Получение карт по колоде
     *
     * @param library library
     * @return ld
     */
    public LiveData<List<CardForLibrary>> getCardsByLibrary(long library) {
        return mDao.getCardsByLibrary(library);
    }

    /**
     * Список желаемого
     * @return ld
     */
    public LiveData<List<Card>> getWishList(){
        return mDao.getWishList();
    }

    public LiveData<Card> getCard(String id) {
        return mDao.getCard(id);
    }

    public LiveData<CardExists> getCardExists(String id) {
        return mDao.getCardExists(id);
    }

    public LiveData<List<Card>> getCardsBySet(String set){
        return mDao.getCardsBySet(set);
    }

    /**
     * Получение колод, в которые входит карта
     *
     * @param id id
     * @return ld
     */
    public LiveData<List<CardLibraryInfo>> getLibrariesByCard(String id) {
        return mDao.getLibrariesByCard(id);
    }

    public LiveData<List<CardManaState>> getLibraryManaState(long library) {
        return mDao.getLibraryManaState(library);
    }

    public LiveData<List<CardColorState>> getLibraryColorState(long library) {
        return mDao.getLibraryColorState(library);
    }

    private void getFilterParams(MutableLiveData<Filter> filterLiveData) {
        mAppExecutors.diskIO().execute(() -> {
            Filter filter = new Filter();

            filter.colors = getColors();
            filter.subtypes = getSubtypes();
            filter.rarities = getRarities();
            filter.sets = getSetNames();
            filter.types = getTypes();

            filterLiveData.postValue(filter);
        });
    }

    private String[] getRarities() {
        String[] strings = new String[6];
        strings[0] = "Basic Land";
        strings[1] = "Common";
        strings[2] = "Uncommon";
        strings[3] = "Rare";
        strings[4] = "Mythic Rare";
        strings[5] = "Special";
        return strings;
    }

    private String[] getTypes() {
        List<Type> list = mDao.getTypes();
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = list.get(i).type;
        }
        return strings;
    }

    private String[] getSubtypes() {
        List<Subtype> subtypes = mDao.getSubtypes();
        String[] strings = new String[subtypes.size()];
        for (int i = 0; i < subtypes.size(); i++) {
            strings[i] = subtypes.get(i).subtype;
        }
        return strings;
    }


    /**
     * Список сетов
     * @return Список сетов
     */
    private String[] getSetNames() {
        List<SetName> setNames = mDao.getSetNames();
        String[] strings = new String[setNames.size()];
        for (int i = 0; i < setNames.size(); i++) {
            strings[i] = setNames.get(i).setName;
        }
        return strings;
    }

    /**
     * Список цветов
     * @return Список цветов
     */
    private String[] getColors() {
        List<Color> colors = mDao.getColors();
        String[] strings = new String[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            strings[i] = colors.get(i).color;
        }
        return strings;
    }

    public LiveData<Filter> getFilter() {
        MutableLiveData<Filter> filter = new MutableLiveData<>();
        getFilterParams(filter);
        return filter;
    }

}
