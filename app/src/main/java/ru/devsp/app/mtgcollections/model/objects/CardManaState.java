package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;

/**
 * Информация по мана стоимости карт в колоде
 * Created by gen on 06.10.2017.
 */
public class CardManaState {

    @ColumnInfo(name = "cmc")
    public int cmc;

    @ColumnInfo(name = "count")
    public int count;

    @ColumnInfo(name = "creatures")
    public int creatures;
}
