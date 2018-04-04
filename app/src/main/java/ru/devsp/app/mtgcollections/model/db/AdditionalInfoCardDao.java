package ru.devsp.app.mtgcollections.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import ru.devsp.app.mtgcollections.model.objects.Color;
import ru.devsp.app.mtgcollections.model.objects.Subtype;
import ru.devsp.app.mtgcollections.model.objects.Supertype;
import ru.devsp.app.mtgcollections.model.objects.Type;


/**
 * Цвет, тип, подтипы, суперсипы карты
 * Created by gen on 31.08.2017.
 */
@Dao
public interface AdditionalInfoCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Color item);

    @Query("DELETE FROM Color where card_id = :card")
    void clearColor(String card);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Type item);

    @Query("DELETE FROM Type where card_id = :card")
    void clearType(String card);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Subtype item);

    @Query("DELETE FROM Subtype where card_id = :card")
    void clearSubtype(String card);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Supertype item);

    @Query("DELETE FROM Supertype where card_id = :card")
    void clearSupertype(String card);
}
