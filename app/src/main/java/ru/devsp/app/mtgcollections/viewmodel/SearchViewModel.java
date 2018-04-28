package ru.devsp.app.mtgcollections.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.app.mtgcollections.model.tools.Resource;
import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.model.objects.CardExists;
import ru.devsp.app.mtgcollections.model.objects.Library;
import ru.devsp.app.mtgcollections.model.objects.LibraryCard;
import ru.devsp.app.mtgcollections.model.objects.Wish;
import ru.devsp.app.mtgcollections.repository.AdditionalInfoCardRepository;
import ru.devsp.app.mtgcollections.repository.CardLocalRepository;
import ru.devsp.app.mtgcollections.repository.CardRepository;
import ru.devsp.app.mtgcollections.repository.LibrariesRepository;
import ru.devsp.app.mtgcollections.repository.LibraryCardRepository;
import ru.devsp.app.mtgcollections.repository.WishRepository;
import ru.devsp.app.mtgcollections.tools.AbsentLiveData;

/**
 * Created by gen on 31.08.2017.
 */

public class SearchViewModel extends ViewModel {

    private CardRepository mCardRepository;
    private CardLocalRepository mCardLocalRepository;
    private LibraryCardRepository mLibraryCardRepository;
    private AdditionalInfoCardRepository mAdditionalInfoCardRepository;
    private WishRepository mWishRepository;

    /**
     * Колоды
     */
    private LiveData<List<Library>> mAllLibraries;

    /**
     * Загруженная из сети карта
     */
    private final LiveData<Resource<List<Card>>> mCard;
    private final MutableLiveData<SearchCardParams> mSearchCardParams = new MutableLiveData<>();

    /**
     * Локальная копия карты
     */
    private final LiveData<CardExists> mCardExist;
    private final MutableLiveData<String> mCardExistSwitcher = new MutableLiveData<>();

    @Inject
    SearchViewModel(CardRepository cardRepository, CardLocalRepository cardLocalRepository,
                    LibrariesRepository librariesRepository, LibraryCardRepository libraryCardRepository,
                    AdditionalInfoCardRepository additionalInfoCardRepository,
                    WishRepository wishRepository) {
        mCardRepository = cardRepository;
        mCardLocalRepository = cardLocalRepository;
        mLibraryCardRepository = libraryCardRepository;
        mAdditionalInfoCardRepository = additionalInfoCardRepository;
        mWishRepository = wishRepository;

        mCard = Transformations.switchMap(mSearchCardParams, params -> {
            if (params == null) {
                return AbsentLiveData.Companion.create();
            } else if (params.name != null) {
                return mCardRepository.getCardByName(params.set, params.name);
            }
            return mCardRepository.getCard(params.set, params.number);
        });

        mCardExist = Transformations.switchMap(mCardExistSwitcher, id -> {
            if (id == null) {
                return AbsentLiveData.Companion.create();
            }
            return cardLocalRepository.getCardExists(id);
        });

        mAllLibraries = librariesRepository.getAllLibraries();
    }

    public LiveData<List<Library>> getLibraries() {
        return mAllLibraries;
    }

    /**
     * Поиск карты
     *
     * @param set    Выпуск
     * @param number Номер в выпуске
     */
    public void search(String set, String number, String name) {
        SearchCardParams update = new SearchCardParams(set, number, name);
        mSearchCardParams.setValue(update);
    }

    /**
     * Загруженая из сети карта
     *
     * @return Карта
     */
    public LiveData<Resource<List<Card>>> getCard() {
        return mCard;
    }

    /**
     * Локальная копия карты
     *
     * @return Локальная карта
     */
    public LiveData<CardExists> getCardExist() {
        return mCardExist;
    }

    /**
     * Проверка сохраненной карты
     *
     * @param id Карта
     */
    public void checkCard(String id) {
        mCardExistSwitcher.setValue(id);
    }

    /**
     * Сохранение локальной копии
     *
     * @param card Полученная из сети карта
     */
    public void save(Card card) {
        mCardLocalRepository.save(card);
        mAdditionalInfoCardRepository.save(card);
    }

    /**
     * Сохранение в списке желаний
     * @param item
     * @return
     */
    public LiveData<Long> addToWish(Wish item) {
        return mWishRepository.save(item);
    }

    public void deleteWish(Wish item){
        mWishRepository.delete(item);
    }

    /**
     * Добавление в библиотеку
     *
     * @param item Связь карты и колоды
     */
    public void addToLibrary(LibraryCard item) {
        mLibraryCardRepository.save(item);
    }


    private static class SearchCardParams {
        final String set;
        final String number;
        final String name;

        SearchCardParams(String set, @Nullable String number, @Nullable String name) {
            this.number = number;
            this.set = set;
            this.name = name;
        }

    }

}
