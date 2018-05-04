package ru.devsp.app.mtgcollections.model.db


import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryInfo


/**
 * Получение данных по сохраненным картам
 * Created by gen on 31.08.2017.
 */
@Dao
interface LibraryDao {

    @get:Query("SELECT l.* FROM Library l")
    val librariesList: LiveData<List<Library>>

    @get:Query("SELECT l.*, SUM(lc.count) cardsCount " +
            "FROM Library l, LibraryCard lc " +
            "WHERE l.id = lc.library_id " +
            "GROUP BY l.id")
    val librariesInfoList: LiveData<List<LibraryInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Library)

    @Update
    fun update(item: Library)

    @Delete
    fun delete(item: Library)
}
