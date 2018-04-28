package ru.devsp.app.mtgcollections.repository.bound;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.devsp.app.mtgcollections.model.api.SetsApi;
import ru.devsp.app.mtgcollections.model.objects.Set;
import ru.devsp.app.mtgcollections.model.tools.ApiResponse;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Получение и кэширование сетов
 * Created by gen on 14.09.2017.
 */

public class SetsBound extends NetworkBound<List<Set>, List<Set>> {

    private List<Set> mCache = new ArrayList<>();

    private SetsApi mSetsApi;

    public SetsBound(AppExecutors appExecutors, SetsApi setsApi) {
        super(appExecutors);
        mSetsApi = setsApi;
    }

    @Override
    protected void saveCallResult(@NonNull List<Set> data) {
        if (!data.isEmpty()) {
            mCache.clear();
            mCache.addAll(data);
        }
    }

    @Override
    protected boolean shouldFetch(@Nullable List<Set> data) {
        return mCache.isEmpty();
    }

    @NonNull
    @Override
    protected LiveData<List<Set>> loadSaved() {
        MutableLiveData<List<Set>> sets = new MutableLiveData<>();
        if (mCache.isEmpty()) {
            sets.setValue(null);
        } else {
            sets.setValue(mCache);
        }
        return sets;
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<List<Set>>> createCall() {
        return mSetsApi.getSets();
    }

}