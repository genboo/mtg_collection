package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;

/**
 * Обертка для информации по колоде
 * Created by gen on 05.10.2017.
 */

public class LibraryInfo {

    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "cardsCount")
    public int cardsCount;

}
