package ru.devsp.app.mtgcollections.model.db


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import ru.devsp.app.mtgcollections.model.objects.LibraryCard


/**
 * Получение данных по сохраненным картам
 * Created by gen on 31.08.2017.
 */
@Dao
interface LibraryCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: LibraryCard)

    @Query("DELETE FROM LibraryCard where library_id = :library AND card_id = :card")
    fun delete(library: Int, card: String)

    @Delete
    fun delete(item: LibraryCard)

    @Update
    fun update(item: LibraryCard)
}
