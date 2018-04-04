package ru.devsp.app.mtgcollections.repository;


import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.app.mtgcollections.model.db.LibraryCardDao;
import ru.devsp.app.mtgcollections.model.objects.LibraryCard;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Обработка связей карт и колод
 * Created by gen on 03.10.2017.
 */
@Singleton
public class LibraryCardRepository {

    private final AppExecutors mAppExecutors;

    private LibraryCardDao mDao;

    @Inject
    LibraryCardRepository(AppExecutors appExecutors, LibraryCardDao dao) {
        mAppExecutors = appExecutors;
        mDao = dao;
    }

    public void save(LibraryCard item) {
        mAppExecutors.diskIO().execute(() -> mDao.insert(item));
    }

    public void update(LibraryCard item) {
        mAppExecutors.diskIO().execute(() -> mDao.update(item));
    }

    public void delete(LibraryCard item){
        mAppExecutors.diskIO().execute(() -> mDao.delete(item));
    }
}
