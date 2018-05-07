package ru.devsp.app.mtgcollections.di.modules


import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import ru.devsp.app.mtgcollections.model.db.*

/**
 * Инициализация базы данных
 * Created by gen on 12.09.2017.
 */
@Module
class DbModule {

    /**
     * Базовый провайдер базы данных
     *
     * @param context Контекст
     * @return База данных
     */
    @Provides
    @Singleton
    internal fun provideDatabase(context: Context): MtgDatabase {
        return Room
                .databaseBuilder(context, MtgDatabase::class.java, DB_NAME)
                .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6
                )
                .build()
    }

    /**
     * Сохраненные карты
     *
     * @param db База данных
     * @return Dao сохраненных карт
     */
    @Provides
    @Singleton
    internal fun provideCardLocalDao(db: MtgDatabase): CardDao {
        return db.cardLocalDao()
    }


    /**
     * Колоды
     *
     * @param db База данных
     * @return Dao колод
     */
    @Provides
    @Singleton
    internal fun provideLibraryDao(db: MtgDatabase): LibraryDao {
        return db.libraryDao()
    }


    /**
     * Связь колод и карт
     *
     * @param db База данных
     * @return Dao списка карт в колодах
     */
    @Provides
    @Singleton
    internal fun provideLibraryCardDao(db: MtgDatabase): LibraryCardDao {
        return db.libraryCardDao()
    }

    /**
     * Цвет, тип, подтипы, суперсипы карты
     *
     * @param db База данных
     * @return Dao доп. информации
     */
    @Provides
    @Singleton
    internal fun provideAdditionalInfoCardDao(db: MtgDatabase): AdditionalInfoCardDao {
        return db.additionalInfoCardDao()
    }


    /**
     * Игроки
     *
     * @param db База данных
     * @return Dao игроков
     */
    @Provides
    @Singleton
    internal fun providePlayerDao(db: MtgDatabase): PlayerDao {
        return db.playerDao()
    }

    /**
     * Списож желаний
     *
     * @param db База данных
     * @return Dao списка желаний
     */
    @Provides
    @Singleton
    internal fun provideWishDao(db: MtgDatabase): WishCardDao {
        return db.wishCardDao()
    }

    /**
     * Списож сетов
     *
     * @param db База данных
     * @return Dao сетов
     */
    @Provides
    @Singleton
    internal fun provideSetsDao(db: MtgDatabase): SetsDao {
        return db.setsDao()
    }

    companion object {

        const val DB_NAME = "mtg"

        /**
         * Список миграций
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Форматированное поле номера
                database.execSQL("ALTER TABLE Card " + " ADD COLUMN numberFormatted TEXT")

                //Таблица игрока
                database.execSQL("CREATE TABLE Player (" +
                        "    id     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "    health INTEGER DEFAULT (20) NOT NULL, " +
                        "    energy INTEGER DEFAULT (0) NOT NULL " +
                        ")")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Родительская карта, нужно для двойных
                database.execSQL("ALTER TABLE Card " + " ADD COLUMN card_id TEXT")
                //Эта карта является дочерней
                database.execSQL("ALTER TABLE Card " + " ADD COLUMN child INTEGER DEFAULT (0) NOT NULL")
            }
        }


        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Обновление поля id
                database.execSQL("ALTER TABLE Card " + " RENAME TO Card_temp")

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
                        "PRIMARY KEY ( id ))")

                database.execSQL("INSERT INTO Card ( " +
                        "id, name, imageUrl, text, " +
                        "flavor, number, numberFormatted, setName, [set], " +
                        "type,  manaCost, cmc, rarity, rulesText, count,  card_id, child  ) " +
                        "SELECT id, name, imageUrl, text, " +
                        "flavor, number, numberFormatted,  setName, `set`, " +
                        "type, manaCost, " +
                        "cmc, rarity, rulesText, count, card_id, child FROM Card_temp")

                database.execSQL("DROP TABLE Card_temp")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Таблица виш листа
                database.execSQL("CREATE TABLE Wish (" +
                        "    id     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "    card_id TEXT UNIQUE " +
                        ")")

                database.execSQL("CREATE UNIQUE INDEX index_Wish_card_id ON Wish (card_id)")
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Таблица сетов
                database.execSQL("CREATE TABLE [Set] (" +
                        "    name        TEXT NOT NULL," +
                        "    border      TEXT," +
                        "    releaseDate TEXT NOT NULL," +
                        "    block       TEXT," +
                        "    code        TEXT NOT NULL," +
                        "    PRIMARY KEY (" +
                        "        code" +
                        "    )" +
                        ")")

                database.execSQL("CREATE INDEX index_Set_releaseDate ON [Set] (releaseDate)")
            }
        }
    }
}
