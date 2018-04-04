package ru.devsp.app.mtgcollections.repository;


import android.arch.lifecycle.LiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.app.mtgcollections.model.db.PlayerDao;
import ru.devsp.app.mtgcollections.model.objects.Player;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Репозиторий данных по игрокам
 * Created by gen on 15.10.2017.
 */
@Singleton
public class GameRepository {

    private final AppExecutors mAppExecutors;
    private PlayerDao mPlayerDao;


    @Inject
    GameRepository(AppExecutors appExecutors, PlayerDao playerDao) {
        mPlayerDao = playerDao;
        mAppExecutors = appExecutors;

    }

    public LiveData<Player> getPlayer(int id) {
        return mPlayerDao.getPlayer(id);
    }

    public void save(Player item) {
        mAppExecutors.diskIO().execute(() -> mPlayerDao.insert(item));
    }

    public void update(Player item) {
        mAppExecutors.diskIO().execute(() -> mPlayerDao.update(item));
    }

}
