package ru.devsp.app.mtgcollections.model.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.CardColorState
import ru.devsp.app.mtgcollections.model.objects.CardExists
import ru.devsp.app.mtgcollections.model.objects.CardForLibrary
import ru.devsp.app.mtgcollections.model.objects.CardLibraryInfo
import ru.devsp.app.mtgcollections.model.objects.CardManaState
import ru.devsp.app.mtgcollections.model.objects.Color
import ru.devsp.app.mtgcollections.model.objects.SetName
import ru.devsp.app.mtgcollections.model.objects.Subtype
import ru.devsp.app.mtgcollections.model.objects.Type

/**
 * Получение данных по сохраненным картам
 * Created by gen on 31.08.2017.
 */
@Dao
interface CardDao {

    @get:Query("SELECT c.id, c.name, c.imageUrl, c.setName, c.`set`, c.type, c.card_id, c.child, " +
            "c.rarity, c.text, c.flavor, c.manaCost, c.cmc, c.count, c.rulesText, c.number, " +
            "CASE WHEN c.numberFormatted is null THEN  c.number " +
            "   ELSE c.numberFormatted END numberFormatted " +
            "FROM Card c WHERE c.child = 0 AND c.count > 0 " +
            "ORDER BY c.`setName`, numberFormatted, number limit 500")
    val allCards: LiveData<List<Card>>

    @get:Query("SELECT c.id, c.name, c.imageUrl, c.setName, c.`set`, c.type, c.card_id, c.child, " +
            " c.rarity, c.text, c.flavor, c.manaCost, c.cmc, c.rulesText, c.count, c.number, " +
            " CASE WHEN c.numberFormatted is null THEN  c.number " +
            " ELSE c.numberFormatted END numberFormatted " +
            " FROM Card c, Wish w " +
            " WHERE w.card_id = c.id " +
            " GROUP BY c.id " +
            " ORDER BY c.setName, numberFormatted")
    val wishList: LiveData<List<Card>>

    @Query("SELECT c.id, c.name, c.imageUrl, c.setName, c.`set`, c.type, c.card_id, c.child, " +
            " c.rarity, c.text, c.flavor, c.manaCost, c.cmc, c.rulesText, c.count, c.number, " +
            " CASE WHEN c.numberFormatted is null THEN  c.number " +
            " ELSE c.numberFormatted END numberFormatted " +
            " FROM Card c, Wish w " +
            " WHERE w.card_id = c.id AND c.setName IN (:sets) " +
            " GROUP BY c.id " +
            " ORDER BY c.setName, numberFormatted")
    fun getFilteredWishList(sets: Array<String>): LiveData<List<Card>>

    @get:Query("SELECT col.* FROM Color col GROUP BY col.color ORDER BY col.color")
    val colors: List<Color>

    @get:Query("SELECT type.* FROM Type type GROUP BY type.type ORDER BY type.type")
    val types: List<Type>

    @get:Query("SELECT sub.* FROM Subtype sub GROUP BY sub.subtype ORDER BY sub.subtype")
    val subtypes: List<Subtype>

    @get:Query("SELECT c.`setName` FROM Card c WHERE c.count > 0 GROUP BY c.`set` ORDER BY c.setName")
    val setNames: List<SetName>

    @get:Query("SELECT c.`setName` FROM Card c, Wish w WHERE c.id = w.card_id GROUP BY c.`set` ORDER BY c.setName")
    val wishSetNames: LiveData<List<SetName>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Card)

    @Delete
    fun delete(item: Card)

    @Update
    fun update(item: Card)

    @Query("SELECT c.* FROM Card c WHERE c.id = :id")
    fun getCard(id: String): LiveData<Card>

    @Query("SELECT w.id, c.count, CASE WHEN w.card_id is null THEN  0  " +
            "              ELSE 1 END wish  " +
            "FROM Card c  " +
            "LEFT join Wish w on w.card_id = c.id  " +
            "WHERE c.id = :id")
    fun getCardExists(id: String): LiveData<CardExists>

    @Query("SELECT * FROM Card c WHERE c.`set` = :set AND c.count > 0")
    fun getCardsBySet(set: String): LiveData<List<Card>>

    @Query("SELECT c.id, c.name, c.imageUrl, c.setName, c.`set`, c.type, c.card_id, c.child, " +
            "c.rarity, c.text, c.flavor, c.manaCost, c.cmc, c.rulesText, c.count, c.number, " +
            "CASE WHEN c.numberFormatted is null THEN  c.number " +
            "   ELSE c.numberFormatted END numberFormatted " +
            "FROM Card c " +
            "LEFT JOIN Type type ON c.id = type.card_id " +
            "LEFT JOIN Subtype sub ON c.id = sub.card_id " +
            "LEFT JOIN Color col ON col.card_id = c.id " +
            "WHERE " +
            "    (sub.subtype IN (:subtypes)  OR (sub.subtype is NULL AND (type.type NOT IN ('Instant', 'Enchantment', 'Sorcery') OR type.type IN (:types)))) " +
            "    AND type.type IN (:types)" +
            "    AND (col.color IN (:colors) OR col.color is null) " +
            "    AND c.rarity IN (:rarities) " +
            "    AND c.setName IN (:sets) " +
            "    AND c.child  = 0 AND c.count > 0 " +
            "GROUP BY c.id " +
            "ORDER BY c.setName, numberFormatted, number ")
    fun getFilteredCards(types: Array<String>, subtypes: Array<String>, colors: Array<String>,
                         rarities: Array<String>, sets: Array<String>): LiveData<List<Card>>

    @Query("SELECT c.id, c.name, c.imageUrl, c.setName, c.`set`, c.type, c.card_id, c.child, " +
            " c.rarity, c.text, c.flavor, c.manaCost, c.cmc, c.rulesText, lc.count, c.number, " +
            " CASE WHEN c.numberFormatted is null THEN  c.number " +
            " ELSE c.numberFormatted END numberFormatted, " +
            " MIN(t.type) typeSingle " +
            " FROM Card c, LibraryCard lc, Type t " +
            " WHERE lc.library_id = :library AND lc.card_id = c.id AND t.card_id = c.id " +
            " GROUP BY c.id " +
            " ORDER BY typeSingle, c.setName, numberFormatted")
    fun getCardsByLibrary(library: Long): LiveData<List<CardForLibrary>>

    @Query("SELECT lc.id, l.name, lc.library_id, lc.card_id, lc.count " +
            "FROM LibraryCard lc, Library l " +
            "WHERE lc.library_id = l.id AND lc.card_id = :card")
    fun getLibrariesByCard(card: String): LiveData<List<CardLibraryInfo>>

    @Query("SELECT ac.cmc, ac.count count, IFNULL(acr.count, 0) creatures " +
            "FROM (SELECT c.cmc, SUM(lc.count) count " +
            "    FROM Card c, LibraryCard lc " +
            "    WHERE lc.card_id = c.id AND lc.library_id = :library AND c.cmc > 0 " +
            "    GROUP BY c.cmc) as ac " +
            "LEFT JOIN " +
            "    (SELECT c.cmc, SUM(lc.count) count " +
            "    FROM Card c, LibraryCard lc, Type t " +
            "    WHERE lc.card_id = c.id " +
            "        AND lc.library_id = :library " +
            "        AND c.cmc > 0 " +
            "        AND t.card_id = c.id " +
            "        AND t.type = 'Creature' " +
            "    GROUP BY c.cmc) as acr ON ac.cmc = acr.cmc " +
            "GROUP BY ac.cmc")
    fun getLibraryManaState(library: Long): LiveData<List<CardManaState>>

    @Query("SELECT cl.color, SUM(lc.count) count FROM Card c " +
            "LEFT JOIN Color cl ON c.id = cl.card_id " +
            "JOIN LibraryCard lc ON lc.card_id = c.id " +
            "WHERE lc.library_id = :library AND c.cmc > 0 " +
            "GROUP BY cl.color " +
            "ORDER BY cl.color")
    fun getLibraryColorState(library: Long): LiveData<List<CardColorState>>

    @Query("UPDATE Card SET card_id = :link WHERE id = :parent")
    fun updateLink(parent: String, link: String)

    @Query("UPDATE Card SET child = :child WHERE id = :card")
    fun updateChildState(card: String, child: Boolean)

}
