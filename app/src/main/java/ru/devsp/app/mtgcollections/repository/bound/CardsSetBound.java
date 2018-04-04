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
 * Получение и кэширование карт
 * Created by gen on 22.12.2017.
 */

public class CardsSetBound extends NetworkBoundResource<List<Card>, List<Card>> {

    public static final int PAGES_SIZE = 40;
    private static final int MAX_CACHE_SIZE = 5;

    private static Map<String, List<Card>> mCache = new HashMap<>();
    private static Map<String, Boolean> mDownloadPages = new HashMap<>();

    private CardApi mCardApi;
    private String mSet;
    private int mPage;

    public CardsSetBound(AppExecutors appExecutors, CardApi cardApi) {
        super(appExecutors);
        mCardApi = cardApi;
    }

    public CardsSetBound setParams(String set, int page) {
        mSet = set;
        mPage = page;
        return this;
    }

    @Override
    protected void saveCallResult(@NonNull List<Card> item) {
        if (!item.isEmpty()) {
            if (mCache.size() == MAX_CACHE_SIZE && mPage == 1) {
                mCache.clear();
                mDownloadPages.clear();
            }
            if (mCache.containsKey(mSet)) {
                mCache.get(mSet).addAll(item);
            } else {
                mCache.put(mSet, item);
            }
            mDownloadPages.put(mSet + mPage, true);
        }
    }

    @Override
    protected boolean shouldFetch(@Nullable List<Card> data) {
        return !mDownloadPages.containsKey(mSet + mPage);
    }

    @NonNull
    @Override
    protected LiveData<List<Card>> loadSaved() {
        MutableLiveData<List<Card>> card = new MutableLiveData<>();
        if (mCache.containsKey(mSet)) {
            card.setValue(mCache.get(mSet));
        } else {
            card.setValue(null);
        }
        return card;
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<List<Card>>> createCall() {
        return mCardApi.getCardsBySet(mSet, mPage, PAGES_SIZE);
    }

}