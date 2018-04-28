package ru.devsp.app.mtgcollections.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.app.mtgcollections.model.tools.Resource;
import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.model.objects.CardLibraryInfo;
import ru.devsp.app.mtgcollections.model.objects.Library;
import ru.devsp.app.mtgcollections.model.objects.LibraryCard;
import ru.devsp.app.mtgcollections.repository.CardLocalRepository;
import ru.devsp.app.mtgcollections.repository.CardRepository;
import ru.devsp.app.mtgcollections.repository.LibrariesRepository;
import ru.devsp.app.mtgcollections.repository.LibraryCardRepository;
import ru.devsp.app.mtgcollections.tools.AbsentLiveData;

/**
 * Управление карточкой карты
 * Created by gen on 31.08.2017.
 */

public class CardViewModel extends ViewModel {

    private CardLocalRepository mCardLocalRepository;
    private LibraryCardRepository mLibraryCardRepository;
    private CardRepository mCardRepository;

    private LiveData<Card> mCard;
    private LiveData<Card> mCardSide;
    private final LiveData<Resource<List<Card>>> mCardNetwork;

    private LiveData<List<CardLibraryInfo>> mCardLibraryInfo;
    private LiveData<List<Library>> mAllLibraries;
    private final MutableLiveData<String> mSwitcher = new MutableLiveData<>();
    private final MutableLiveData<String> mSwitcherChild = new MutableLiveData<>();


    @Inject
    CardViewModel(CardLocalRepository cardLocalRepository,
                  LibraryCardRepository libraryCardRepository,
                  LibrariesRepository librariesRepository,
                  CardRepository cardRepository) {
        mCardLocalRepository = cardLocalRepository;
        mLibraryCardRepository = libraryCardRepository;
        mCardRepository = cardRepository;

        mCard = Transformations.switchMap(mSwitcher, id -> {
            if (id == null) {
                return AbsentLiveData.Companion.create();
            } else {
                return mCardLocalRepository.getCard(id);
            }
        });
        mCardSide = Transformations.switchMap(mSwitcherChild, id -> {
            if (id == null) {
                return AbsentLiveData.Companion.create();
            } else {
                return mCardLocalRepository.getCard(id);
            }
        });
        mCardLibraryInfo = Transformations.switchMap(mSwitcher, id -> {
            if (id == null) {
                return AbsentLiveData.Companion.create();
            } else {
                return mCardLocalRepository.getLibrariesByCard(id);
            }
        });

        mCardNetwork = Transformations.switchMap(mSwitcher, id -> {
            if (id == null) {
                return AbsentLiveData.Companion.create();
            }
            return mCardRepository.getCard(id);
        });

        mAllLibraries = librariesRepository.getAllLibraries();

    }

    public void setId(String id) {
        mSwitcher.setValue(id);
    }
    public void setIdChild(String id) {
        mSwitcherChild.setValue(id);
    }

    /**
     * Выбранная карта
     * @return Выбранная карта
     */
    public LiveData<Card> getCard() {
        return mCard;
    }

    /**
     * Обратная сторона
     * @return Обратная сторона
     */
    public LiveData<Card> getCardSide() {
        return mCardSide;
    }

    /**
     * Карта из сети (для обновления информации)
     * @return Карта из сети
     */
    public LiveData<Resource<List<Card>>> getCardNetwork() {
        return mCardNetwork;
    }

    /**
     * Все колоды, в которые входит карта
     * @return Все колоды, в которые входит карта
     */
    public LiveData<List<CardLibraryInfo>> getLibrariesByCard() {
        return mCardLibraryInfo;
    }

    /**
     * Сохранение количества карт в колоде
     * @param item Карта
     */
    public void saveCount(LibraryCard item) {
        if (item.count > 0) {
            mLibraryCardRepository.update(item);
        } else {
            mLibraryCardRepository.delete(item);
        }
    }

    /**
     * Обновление информации по карте
     * @param item карта
     */
    public void updateCard(Card item) {
        mCardLocalRepository.update(item);
    }

    /**
     * Обновление связи карт
     * @param parent родитель
     * @param link ссылка
     */
    public void updateLink(String parent, String link) {
        mCardLocalRepository.updateLink(parent, link);
        mCardLocalRepository.setChild(link);
    }

    public LiveData<List<Library>> getLibraries() {
        return mAllLibraries;
    }

    public void addToLibrary(LibraryCard item) {
        mLibraryCardRepository.save(item);
    }
}
