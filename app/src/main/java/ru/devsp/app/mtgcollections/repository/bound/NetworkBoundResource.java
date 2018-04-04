package ru.devsp.app.mtgcollections.repository.bound;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import ru.devsp.app.mtgcollections.model.api.ApiResponse;
import ru.devsp.app.mtgcollections.model.api.tools.Resource;
import ru.devsp.app.mtgcollections.tools.AppExecutors;
import ru.devsp.app.mtgcollections.tools.Logger;

/**
 * Получение данных из сети и их кэширование
 * Created by gen on 31.08.2017.
 */

public abstract class NetworkBoundResource<R, Q> {


    private final AppExecutors mAppExecutors;
    final MediatorLiveData<Resource<R>> result = new MediatorLiveData<>();


    @MainThread
    NetworkBoundResource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public void create() {
        result.setValue(Resource.loading(null));
        LiveData<R> dbSource = loadSaved();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> result.setValue(Resource.success(newData)));
            }
        });
    }

    public LiveData<Resource<R>> asLiveData() {
        return result;
    }

    private void fetchFromNetwork(final LiveData<R> dbSource) {
        LiveData<ApiResponse<Q>> apiResponse = createCall();

        result.addSource(dbSource, newData -> result.setValue(Resource.loading(newData)));
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            if (response != null && response.body != null) {
                mAppExecutors.networkIO().execute(() -> {
                    saveCallResult(processResponse(response));
                    mAppExecutors.mainThread().execute(() ->
                            result.addSource(loadSaved(),
                                    newData ->
                                            result.setValue(Resource.success(newData)))
                    );
                });
            } else {
                if(response != null) {
                    onFetchFailed(response.errorMessage);
                }
                result.addSource(dbSource,
                        newData -> result.setValue(Resource.error("Ошибка получения данных", newData)));
            }
        });
    }

    private void onFetchFailed(String message) {
        Logger.INSTANCE.e(message);
    }

    @WorkerThread
    private Q processResponse(ApiResponse<Q> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull Q item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable R data);

    @NonNull
    @MainThread
    protected abstract LiveData<R> loadSaved();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<Q>> createCall();

}
