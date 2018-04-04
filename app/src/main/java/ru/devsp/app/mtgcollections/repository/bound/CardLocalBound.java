package ru.devsp.app.mtgcollections.repository.bound;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.devsp.app.mtgcollections.model.api.ApiResponse;
import ru.devsp.app.mtgcollections.model.api.CardApi;
import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Получение и кэширование карт по id
 * Created by gen on 14.09.2017.
 */

public class CardLocalBound extends NetworkBoundResource<List<Card>, List<Card>> {

    private static final int MAX_CACHE_SIZE = 100;

    private static Map<String, List<Card>> mCache = new HashMap<>();

    private CardApi mCardApi;
    private String mId;

    public CardLocalBound(AppExecutors appExecutors, CardApi cardApi) {
        super(appExecutors);
        mCardApi = cardApi;
    }

    public CardLocalBound setParams(String id) {
        mId = id;
        return this;
    }

    @Override
    protected void saveCallResult(@NonNull List<Card> item) {
        if (!item.isEmpty()) {
            if (mCache.size() == MAX_CACHE_SIZE) {
                mCache.clear();
            }
            mCache.put(mId, item);
        }
    }

    @Override
    protected boolean shouldFetch(@Nullable List<Card> data) {
        return !mCache.containsKey(mId);
    }

    @NonNull
    @Override
    protected LiveData<List<Card>> loadSaved() {
        MutableLiveData<List<Card>> card = new MutableLiveData<>();
        if (mCache.containsKey(mId)) {
            card.setValue(mCache.get(mId));
        } else {
            card.setValue(null);
        }
        return card;
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<List<Card>>> createCall() {
        return mCardApi.getCard(mId);
    }

}