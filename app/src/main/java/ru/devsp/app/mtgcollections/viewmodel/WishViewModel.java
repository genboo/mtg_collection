package ru.devsp.app.mtgcollections.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.repository.CardLocalRepository;

/**
 * Коллекция карт
 * Created by gen on 31.08.2017.
 */

public class WishViewModel extends ViewModel {

    private CardLocalRepository mCardLocalRepository;
    private LiveData<List<Card>> mCards;

    @Inject
    WishViewModel(CardLocalRepository cardLocalRepository) {
        mCardLocalRepository = cardLocalRepository;
        mCards = mCardLocalRepository.getWishList();
    }

    public LiveData<List<Card>> getWishList(){
        return mCards;
    }

}
