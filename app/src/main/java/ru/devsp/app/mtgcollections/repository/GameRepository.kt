package ru.devsp.app.mtgcollections.repository

import android.arch.lifecycle.LiveData

import javax.inject.Inject
import javax.inject.Singleton

import ru.devsp.app.mtgcollections.model.db.PlayerDao
import ru.devsp.app.mtgcollections.model.objects.Player
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Репозиторий данных по игрокам
 * Created by gen on 15.10.2017.
 */
@Singleton
class GameRepository @Inject
internal constructor(private val appExecutors: AppExecutors, private val playerDao: PlayerDao) {

    fun getPlayer(id: Int): LiveData<Player> {
        return playerDao.getPlayer(id)
    }

    fun save(item: Player) {
        appExecutors.diskIO().execute { playerDao.insert(item) }
    }

    fun update(item: Player) {
        appExecutors.diskIO().execute { playerDao.update(item) }
    }

}
