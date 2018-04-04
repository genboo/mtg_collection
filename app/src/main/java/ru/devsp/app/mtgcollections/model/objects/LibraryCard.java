package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Связь карты и колоды
 * Created by gen on 04.10.2017.
 */

@Entity (indices = {@Index(value = {"library_id", "card_id"}, unique = true)})
public class LibraryCard {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "library_id")
    public long libraryId;

    @ColumnInfo(name = "card_id")
    public String cardId;

    public int count;

}
