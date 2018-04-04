package ru.devsp.app.mtgcollections.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.SparseArray;


import javax.inject.Inject;

import ru.devsp.app.mtgcollections.model.objects.Player;
import ru.devsp.app.mtgcollections.repository.GameRepository;

/**
 * Управление игровыми показателями
 * Created by gen on 15.10.2017.
 */

public class GameViewModel extends ViewModel {

    private GameRepository mGameRepository;
    private SparseArray<LiveData<Player>> mPlayers;

    @Inject
    GameViewModel(GameRepository gameRepository) {
        mGameRepository = gameRepository;
        mPlayers = new SparseArray<>();
    }

    public LiveData<Player> getPlayer(int id){
        LiveData<Player> playerCached = mPlayers.get(id);
        if(playerCached != null){
            return playerCached;
        }
        LiveData<Player> player = mGameRepository.getPlayer(id);
        mPlayers.put(id, player);
        return player;
    }

    public void save(Player item){
        mGameRepository.save(item);
    }

}
