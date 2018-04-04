package ru.devsp.app.mtgcollections.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import ru.devsp.app.mtgcollections.model.objects.Player;


/**
 * Получение данных по игрокам
 * Created by gen on 15.10.2017.
 */
@Dao
public interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Player item);

    @Update
    void update(Player item);

    @Delete
    void delete(Player item);

    @Query("SELECT * FROM Player WHERE id = :id")
    LiveData<Player> getPlayer(int id);

}
