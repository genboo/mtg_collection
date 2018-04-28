package ru.devsp.app.mtgcollections.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.app.mtgcollections.model.tools.Resource;
import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.repository.CardLocalRepository;
import ru.devsp.app.mtgcollections.repository.CardRepository;
import ru.devsp.app.mtgcollections.tools.AbsentLiveData;

/**
 * Спойлеры по сету
 * Created by gen on 22.12.2017.
 */

public class SpoilersViewModel extends ViewModel {

    private CardRepository mCardRepository;
    private CardLocalRepository mCardLocalRepository;

    private final LiveData<Resource<List<Card>>> mCards;
    private final LiveData<List<Card>> mCardsBySet;
    private final MutableLiveData<Params> mSwitcher = new MutableLiveData<>();


    @Inject
    SpoilersViewModel(CardRepository cardRepository, CardLocalRepository cardLocalRepository) {
        mCardRepository = cardRepository;
        mCardLocalRepository = cardLocalRepository;

        mCards = Transformations.switchMap(mSwitcher, params -> {
            if (params == null) {
                return AbsentLiveData.Companion.create();
            }
            return mCardRepository.getCardsBySet(params.set, params.page);
        });
        mCardsBySet = Transformations.switchMap(mSwitcher, params -> {
            if (params == null) {
                return AbsentLiveData.Companion.create();
            }
            return mCardLocalRepository.getCardsBySet(params.set);
        });

    }

    public LiveData<Resource<List<Card>>> getCards() {
        return mCards;
    }

    public LiveData<List<Card>> getCardsBySet(){
        return mCardsBySet;
    }

    public void setParams(String set, int page){
        mSwitcher.postValue(new Params(set, page));
    }

    private class Params{
        private int page;
        private String set;

        Params(String set, int page){
            this.page = page;
            this.set = set;
        }
    }

}
