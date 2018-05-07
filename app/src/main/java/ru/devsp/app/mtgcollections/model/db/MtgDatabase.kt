package ru.devsp.app.mtgcollections.model.db


import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.Color
import ru.devsp.app.mtgcollections.model.objects.Library
import ru.devsp.app.mtgcollections.model.objects.LibraryCard
import ru.devsp.app.mtgcollections.model.objects.Player
import ru.devsp.app.mtgcollections.model.objects.Set
import ru.devsp.app.mtgcollections.model.objects.Subtype
import ru.devsp.app.mtgcollections.model.objects.Supertype
import ru.devsp.app.mtgcollections.model.objects.Type
import ru.devsp.app.mtgcollections.model.objects.Wish

/**
 * База данных
 * Created by gen on 31.08.2017.
 */
@Database(version = 6, exportSchema = false, entities = [
    Card::class,
    Set::class,
    Library::class,
    LibraryCard::class,
    Color::class,
    Type::class,
    Subtype::class,
    Supertype::class,
    Player::class,
    Wish::class])
abstract class MtgDatabase : RoomDatabase() {

    abstract fun cardLocalDao(): CardDao

    abstract fun libraryDao(): LibraryDao

    abstract fun libraryCardDao(): LibraryCardDao

    abstract fun additionalInfoCardDao(): AdditionalInfoCardDao

    abstract fun playerDao(): PlayerDao

    abstract fun wishCardDao(): WishCardDao

    abstract fun setsDao(): SetsDao

}
