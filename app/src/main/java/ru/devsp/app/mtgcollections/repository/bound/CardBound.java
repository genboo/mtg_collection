package ru.devsp.app.mtgcollections.repository.bound;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.devsp.app.mtgcollections.model.api.CardApi;
import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.model.tools.ApiResponse;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Получение и кэширование карт
 * Created by gen on 14.09.2017.
 */

public class CardBound extends NetworkBound<List<Card>, List<Card>> {

    private static final int MAX_CACHE_SIZE = 100;

    private static Map<String, Card> mCache = new HashMap<>();

    private CardApi mCardApi;
    private String mSet;
    private String mNumber;

    public CardBound(AppExecutors appExecutors, CardApi cardApi) {
        super(appExecutors);
        mCardApi = cardApi;
    }

    public CardBound setParams(String set, String number) {
        mSet = set;
        mNumber = number;
        return this;
    }

    @Override
    protected void saveCallResult(@NonNull List<Card> item) {
        if (!item.isEmpty()) {
            if (mCache.size() == MAX_CACHE_SIZE) {
                mCache.clear();
            }
            mCache.put(mSet + mNumber, item.get(0));
        }
    }

    @Override
    protected boolean shouldFetch(@Nullable List<Card> data) {
        return !mCache.containsKey(mSet + mNumber);
    }

    @NonNull
    @Override
    protected LiveData<List<Card>> loadSaved() {
        MutableLiveData<List<Card>> card = new MutableLiveData<>();
        if (mCache.containsKey(mSet + mNumber)) {
            List<Card> cards = new ArrayList<>();
            cards.add(mCache.get(mSet + mNumber));
            card.setValue(cards);
        } else {
            card.setValue(null);
        }
        return card;
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<List<Card>>> createCall() {
        return mCardApi.getCard(mSet, mNumber);
    }

}