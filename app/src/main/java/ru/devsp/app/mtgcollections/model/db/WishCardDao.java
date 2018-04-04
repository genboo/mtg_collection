package ru.devsp.app.mtgcollections.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import ru.devsp.app.mtgcollections.model.objects.Wish;


/**
 * Получение данных по списку желаемого
 * Created by gen on 21.12.2017.
 */
@Dao
public interface WishCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Wish item);

    @Delete
    void delete(Wish item);

    @Update
    void update(Wish item);
}
