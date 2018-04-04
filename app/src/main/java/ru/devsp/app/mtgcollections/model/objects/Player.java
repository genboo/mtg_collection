package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Игрок
 * Created by gen on 15.10.2017.
 */
@Entity
public class Player {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public int health;

    public int energy;
}
