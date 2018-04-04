package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;

/**
 * Информация по цветам в колоде
 * Created by gen on 07.10.2017.
 */

public class CardColorState {
    @ColumnInfo(name = "color")
    public String color;

    @ColumnInfo(name = "count")
    public int count;
}
