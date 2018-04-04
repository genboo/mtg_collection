package ru.devsp.app.mtgcollections.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.devsp.app.mtgcollections.model.objects.Library;
import ru.devsp.app.mtgcollections.model.objects.LibraryInfo;


/**
 * Получение данных по сохраненным картам
 * Created by gen on 31.08.2017.
 */
@Dao
public interface LibraryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Library item);

    @Update
    void update(Library item);

    @Query("SELECT l.* FROM Library l")
    LiveData<List<Library>> getLibrariesList();

    @Query("SELECT l.*, SUM(lc.count) cardsCount " +
            "FROM Library l, LibraryCard lc " +
            "WHERE l.id = lc.library_id " +
            "GROUP BY l.id")
    LiveData<List<LibraryInfo>> getLibrariesInfoList();

    @Delete
    void delete(Library item);
}
