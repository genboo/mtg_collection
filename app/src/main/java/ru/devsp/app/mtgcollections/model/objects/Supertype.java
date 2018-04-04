package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Супертип карты
 * Created by gen on 05.10.2017.
 */
@Entity
public class Supertype {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "card_id")
    public String cardId;

    public String supertype;
}
