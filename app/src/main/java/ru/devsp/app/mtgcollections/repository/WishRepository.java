package ru.devsp.app.mtgcollections.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.app.mtgcollections.model.db.WishCardDao;
import ru.devsp.app.mtgcollections.model.objects.Wish;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Репозиторий данных по списку желаний
 * Created by gen on 21.12.2017.
 */
@Singleton
public class WishRepository {

    private final AppExecutors mAppExecutors;
    private WishCardDao mWishDao;

    @Inject
    WishRepository(AppExecutors appExecutors, WishCardDao wishDao) {
        mWishDao = wishDao;
        mAppExecutors = appExecutors;
    }

    public LiveData<Long> save(Wish item) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        mAppExecutors.diskIO().execute(() -> {
            long id = mWishDao.insert(item);
            mAppExecutors.mainThread().execute(() -> result.postValue(id));
        });
        return result;
    }

    public void delete(Wish item) {
        mAppExecutors.diskIO().execute(() -> mWishDao.delete(item));
    }

}
