package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Список желаемого
 * Created by gen on 21.12.2017.
 */
@Entity(indices = {@Index(value = {"card_id"}, unique = true)})
public class Wish {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "card_id")
    public String cardId;
}
