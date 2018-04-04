package ru.devsp.app.mtgcollections.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.app.mtgcollections.model.api.SetsApi;
import ru.devsp.app.mtgcollections.model.api.tools.Resource;
import ru.devsp.app.mtgcollections.model.objects.Set;
import ru.devsp.app.mtgcollections.repository.bound.SetsBound;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Репозиторий данных по сетам
 * Created by gen on 22.12.2017.
 */
@Singleton
public class SetsRepository {

    private SetsBound bound;

    @Inject
    SetsRepository(AppExecutors appExecutors, SetsApi setsApi) {
        bound = new SetsBound(appExecutors, setsApi);
    }

    /**
     * Получение списка сетов
     *
     * @return
     */
    public LiveData<Resource<List<Set>>> getSets() {
        bound.create();
        return bound.asLiveData();
    }

}
