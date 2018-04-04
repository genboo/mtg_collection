package ru.devsp.app.mtgcollections.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.model.objects.Color;
import ru.devsp.app.mtgcollections.model.objects.Library;
import ru.devsp.app.mtgcollections.model.objects.LibraryCard;
import ru.devsp.app.mtgcollections.model.objects.Player;
import ru.devsp.app.mtgcollections.model.objects.Subtype;
import ru.devsp.app.mtgcollections.model.objects.Supertype;
import ru.devsp.app.mtgcollections.model.objects.Type;
import ru.devsp.app.mtgcollections.model.objects.Wish;

/**
 * База данных
 * Created by gen on 31.08.2017.
 */
@Database(version = 5, exportSchema = false, entities = {
        Card.class,
        Library.class,
        LibraryCard.class,
        Color.class,
        Type.class,
        Subtype.class,
        Supertype.class,
        Player.class,
        Wish.class,
})
public abstract class MtgDatabase extends RoomDatabase {

    public abstract CardDao cardLocalDao();

    public abstract LibraryDao libraryDao();

    public abstract LibraryCardDao libraryCardDao();

    public abstract AdditionalInfoCardDao additionalInfoCardDao();

    public abstract PlayerDao playerDao();

    public abstract WishCardDao wishCardDao();

}
