package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;


/**
 * Карта для списка библиотеки
 * Created by gen on 01.11.2017.
 */

public class CardForLibrary {

    @PrimaryKey
    @SerializedName("multiverseid")
    public String id;

    public String name;

    public String imageUrl;

    public String text;

    public String flavor;

    public String number;

    public String numberFormatted;

    public String setName;

    public String set;

    public String type;

    public String typeSingle;

    public String manaCost;

    public String cmc;

    public String rarity;

    public String rulesText = "";

    @ColumnInfo(name = "card_id")
    public String parent;

    public boolean child;

    public int count;

    public CardForLibrary(String id) {
        this.id = id;
    }

    public int getRarityColor(){
        return Card.getRarityColor(rarity);
    }

    public int getSetIcon(){
        return Card.getSetIcon(set);
    }
}
