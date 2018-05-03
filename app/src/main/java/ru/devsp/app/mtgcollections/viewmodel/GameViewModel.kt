package ru.devsp.app.mtgcollections.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.util.SparseArray


import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.objects.Player
import ru.devsp.app.mtgcollections.repository.GameRepository

/**
 * Управление игровыми показателями
 * Created by gen on 15.10.2017.
 */

class GameViewModel @Inject
internal constructor(private val gameRepository: GameRepository) : ViewModel() {
    private val players: SparseArray<LiveData<Player>> = SparseArray()

    fun getPlayer(id: Int): LiveData<Player> {
        val playerCached = players.get(id)
        if (playerCached != null) {
            return playerCached
        }
        val player = gameRepository.getPlayer(id)
        players.put(id, player)
        return player
    }

    fun save(item: Player) {
        gameRepository.save(item)
    }

}