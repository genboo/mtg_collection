package ru.devsp.app.mtgcollections.model.db


import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import ru.devsp.app.mtgcollections.model.objects.Player


/**
 * Получение данных по игрокам
 * Created by gen on 15.10.2017.
 */
@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Player)

    @Update
    fun update(item: Player)

    @Delete
    fun delete(item: Player)

    @Query("SELECT * FROM Player WHERE id = :id")
    fun getPlayer(id: Int): LiveData<Player>

}
