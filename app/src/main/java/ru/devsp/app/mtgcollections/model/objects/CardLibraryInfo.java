package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;

/**
 * Информация колодах, в которых содежрится карта
 * Created by gen on 05.10.2017.
 */

public class CardLibraryInfo {

    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "library_id")
    public long libraryId;

    @ColumnInfo(name = "card_id")
    public String cardId;

    @ColumnInfo(name = "count")
    public int count;
}
