package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


/**
 * Колоды
 * Created by gen on 04.10.2017.
 */
@Entity
public class Library {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    @Ignore
    public Library(long id) {
        this.id = id;
    }

    public Library(String name) {
        this.name = name;
    }
}
