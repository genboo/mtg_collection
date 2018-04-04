package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;

/**
 * Названия сетов
 * Created by gen on 12.10.2017.
 */

public class SetName {

    @ColumnInfo(name = "setName")
    public String setName;

}
