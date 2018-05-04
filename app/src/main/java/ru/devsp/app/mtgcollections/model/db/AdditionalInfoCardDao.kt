package ru.devsp.app.mtgcollections.model.db


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import ru.devsp.app.mtgcollections.model.objects.Color
import ru.devsp.app.mtgcollections.model.objects.Subtype
import ru.devsp.app.mtgcollections.model.objects.Supertype
import ru.devsp.app.mtgcollections.model.objects.Type


/**
 * Цвет, тип, подтипы, суперсипы карты
 * Created by gen on 31.08.2017.
 */
@Dao
interface AdditionalInfoCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Color)

    @Query("DELETE FROM Color where card_id = :card")
    fun clearColor(card: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Type)

    @Query("DELETE FROM Type where card_id = :card")
    fun clearType(card: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Subtype)

    @Query("DELETE FROM Subtype where card_id = :card")
    fun clearSubtype(card: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Supertype)

    @Query("DELETE FROM Supertype where card_id = :card")
    fun clearSupertype(card: String)
}
