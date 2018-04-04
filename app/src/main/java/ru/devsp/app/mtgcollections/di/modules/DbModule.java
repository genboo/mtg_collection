package ru.devsp.app.mtgcollections.di.modules;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.devsp.app.mtgcollections.model.db.AdditionalInfoCardDao;
import ru.devsp.app.mtgcollections.model.db.LibraryCardDao;
import ru.devsp.app.mtgcollections.model.db.CardDao;
import ru.devsp.app.mtgcollections.model.db.LibraryDao;
import ru.devsp.app.mtgcollections.model.db.MtgDatabase;
import ru.devsp.app.mtgcollections.model.db.PlayerDao;
import ru.devsp.app.mtgcollections.model.db.WishCardDao;

/**
 * Инициализация базы данных
 * Created by gen on 12.09.2017.
 */
@Module
public class DbModule {

    public static final String DB_NAME = "mtg";

    /**
     * Список миграций
     */
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //Форматированное поле номера
            database.execSQL("ALTER TABLE Card "
                    + " ADD COLUMN numberFormatted TEXT");

            //Таблица игрока
            database.execSQL("CREATE TABLE Player (" +
                    "    id     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "    health INTEGER DEFAULT (20) NOT NULL, " +
                    "    energy INTEGER DEFAULT (0) NOT NULL " +
                    ")");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //Родительская карта, нужно для двойных
            database.execSQL("ALTER TABLE Card "
                    + " ADD COLUMN card_id TEXT");
            //Эта карта является дочерней
            database.execSQL("ALTER TABLE Card "
                    + " ADD COLUMN child INTEGER DEFAULT (0) NOT NULL");
        }
    };


    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //Обновление поля id
            database.execSQL("ALTER TABLE Card "
                    + " RENAME TO Card_temp");

            database.execSQL("CREATE TABLE Card ( " +
                    "id              TEXT    NOT NULL, " +
                    "name            TEXT, " +
                    "imageUrl        TEXT, " +
                    "text            TEXT, " +
                    "flavor          TEXT, " +
                    "number          TEXT, " +
                    "numberFormatted TEXT, " +
                    "setName         TEXT, " +
                    "[set]           TEXT, " +
                    "type            TEXT, " +
                    "manaCost        TEXT, " +
                    "cmc             TEXT, " +
                    "rarity          TEXT, " +
                    "rulesText       TEXT, " +
                    "count           INTEGER NOT NULL, " +
                    "card_id         TEXT, " +
                    "child           INTEGER DEFAULT (0)  NOT NULL, " +
                    "PRIMARY KEY ( id ))");

            database.execSQL("INSERT INTO Card ( " +
                    "id, name, imageUrl, text, " +
                    "flavor, number, numberFormatted, setName, [set], " +
                    "type,  manaCost, cmc, rarity, rulesText, count,  card_id, child  ) " +
                    "SELECT id, name, imageUrl, text, " +
                    "flavor, number, numberFormatted,  setName, `set`, " +
                    "type, manaCost, " +
                    "cmc, rarity, rulesText, count, card_id, child FROM Card_temp");

            database.execSQL("DROP TABLE Card_temp");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //Таблица виш листа
            database.execSQL("CREATE TABLE Wish (" +
                    "    id     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "    card_id TEXT UNIQUE " +
                    ")");

            database.execSQL("CREATE UNIQUE INDEX index_Wish_card_id ON Wish ( " +
                    "    card_id " +
                    ")");
        }
    };

    /**
     * Базовый провайдер базы данных
     *
     * @param context Контекст
     * @return База данных
     */
    @Provides
    @Singleton
    MtgDatabase provideDatabase(Context context) {
        return Room
                .databaseBuilder(context, MtgDatabase.class, DB_NAME)
                .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5
                )
                .build();
    }

    /**
     * Сохраненные карты
     *
     * @param db База данных
     * @return Dao сохраненных карт
     */
    @Provides
    @Singleton
    CardDao provideCardLocalDao(MtgDatabase db) {
        return db.cardLocalDao();
    }


    /**
     * Колоды
     *
     * @param db База данных
     * @return Dao колод
     */
    @Provides
    @Singleton
    LibraryDao provideLibraryDao(MtgDatabase db) {
        return db.libraryDao();
    }


    /**
     * Связь колод и карт
     *
     * @param db База данных
     * @return Dao колод
     */
    @Provides
    @Singleton
    LibraryCardDao provideLibraryCardDao(MtgDatabase db) {
        return db.libraryCardDao();
    }

    /**
     * Цвет, тип, подтипы, суперсипы карты
     *
     * @param db База данных
     * @return Dao колод
     */
    @Provides
    @Singleton
    AdditionalInfoCardDao provideAdditionalInfoCardDao(MtgDatabase db) {
        return db.additionalInfoCardDao();
    }


    /**
     * Игроки
     *
     * @param db База данных
     * @return Dao колод
     */
    @Provides
    @Singleton
    PlayerDao providePlayerDao(MtgDatabase db) {
        return db.playerDao();
    }

    /**
     * Списож желаний
     *
     * @param db База данных
     * @return Dao колод
     */
    @Provides
    @Singleton
    WishCardDao provideWishDao(MtgDatabase db) {
        return db.wishCardDao();
    }
}
