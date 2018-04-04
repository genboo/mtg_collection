package ru.devsp.app.mtgcollections.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.model.objects.CardColorState;
import ru.devsp.app.mtgcollections.model.objects.CardForLibrary;
import ru.devsp.app.mtgcollections.model.objects.CardManaState;
import ru.devsp.app.mtgcollections.model.objects.Filter;
import ru.devsp.app.mtgcollections.model.objects.Library;
import ru.devsp.app.mtgcollections.repository.CardLocalRepository;
import ru.devsp.app.mtgcollections.repository.LibrariesRepository;
import ru.devsp.app.mtgcollections.tools.AbsentLiveData;

/**
 * Коллекция карт
 * Created by gen on 31.08.2017.
 */

public class CollectionViewModel extends ViewModel {

    private CardLocalRepository mCardLocalRepository;
    private LibrariesRepository mLibrariesRepository;

    private LiveData<List<Card>> mFilteredCards;
    private LiveData<Filter> mFilter;
    private LiveData<List<CardForLibrary>> mLibraryCards;
    private LiveData<List<CardManaState>> mCardsManaState;
    private LiveData<List<CardColorState>> mCardsColorState;

    private final MutableLiveData<Long> mSwitcher = new MutableLiveData<>();
    private final MutableLiveData<Filter> mFilterSwitcher = new MutableLiveData<>();

    @Inject
    CollectionViewModel(CardLocalRepository cardLocalRepository,
                        LibrariesRepository librariesRepository) {
        mCardLocalRepository = cardLocalRepository;
        mLibrariesRepository = librariesRepository;

        mLibraryCards = Transformations.switchMap(mSwitcher, id -> {
            if (id == 0) {
                return AbsentLiveData.Companion.create();
            } else {
                return mCardLocalRepository.getCardsByLibrary(id);
            }
        });

        mCardsManaState = Transformations.switchMap(mSwitcher, id -> {
            if (id == 0) {
                return AbsentLiveData.Companion.create();
            } else {
                return mCardLocalRepository.getLibraryManaState(id);
            }
        });

        mCardsColorState = Transformations.switchMap(mSwitcher, id -> {
            if (id == 0) {
                return AbsentLiveData.Companion.create();
            } else {
                return mCardLocalRepository.getLibraryColorState(id);
            }
        });

        mFilter = mCardLocalRepository.getFilter();

        mFilteredCards = Transformations.switchMap(mFilterSwitcher, filter -> {
            if (filter == null) {
                return AbsentLiveData.Companion.create();
            } else if (filter.full) {
                return mCardLocalRepository.getAllCards();
            } else {
                return mCardLocalRepository.getFilteredCard(filter);
            }
        });

    }

    public void setFilter(Filter filter) {
        if (filter.subtypes.length == 0 && filter.colors.length == 0
                && filter.sets.length == 0 && filter.rarities.length == 0
                && filter.types.length == 0) {
            filter.full = true;
        } else if (mFilter.getValue() != null) {
            if (filter.types.length == 0) {
                filter.types = mFilter.getValue().types;
            }
            if (filter.subtypes.length == 0) {
                filter.subtypes = mFilter.getValue().subtypes;
            }
            if (filter.colors.length == 0) {
                filter.colors = mFilter.getValue().colors;
            }
            if (filter.sets.length == 0) {
                filter.sets = mFilter.getValue().sets;
            }
            if (filter.rarities.length == 0) {
                filter.rarities = mFilter.getValue().rarities;
            }
        }
        mFilterSwitcher.postValue(filter);
    }

    /**
     * Отфильтрованные карты
     *
     * @return
     */
    public LiveData<List<Card>> getFilteredCards() {
        return mFilteredCards;
    }

    /**
     * Список доступных фильтров
     *
     * @return
     */
    public LiveData<Filter> getFilter() {
        return mFilter;
    }

    /**
     * Действующие фильтры
     *
     * @return
     */
    public Filter getSelectedFilter() {
        return mFilterSwitcher.getValue();
    }

    public void setLibrary(long id) {
        mSwitcher.setValue(id);
    }

    /**
     * Все карты заданной колоды
     *
     * @return
     */
    public LiveData<List<CardForLibrary>> getCardsByLibrary() {
        return mLibraryCards;
    }

    /**
     * Статистика по мана стоимости карт
     *
     * @return
     */
    public LiveData<List<CardManaState>> getCardsManaState() {
        return mCardsManaState;
    }

    /**
     * Статистика по цветам карт
     *
     * @return
     */
    public LiveData<List<CardColorState>> getCardsColorState() {
        return mCardsColorState;
    }

    public void updateLibrary(Library item) {
        mLibrariesRepository.update(item);
    }


}
