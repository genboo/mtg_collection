package ru.devsp.app.mtgcollections.model.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update

import ru.devsp.app.mtgcollections.model.objects.Wish


/**
 * Получение данных по списку желаемого
 * Created by gen on 21.12.2017.
 */
@Dao
interface WishCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Wish): Long

    @Delete
    fun delete(item: Wish)

    @Update
    fun update(item: Wish)
}
