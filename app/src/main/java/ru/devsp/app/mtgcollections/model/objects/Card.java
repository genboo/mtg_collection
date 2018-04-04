package ru.devsp.app.mtgcollections.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.devsp.app.mtgcollections.R;

/**
 * Карта
 * Created by gen on 02.10.2017.
 */

@Entity
public class Card {

    @Ignore
    private static final Map<String, Integer> RARITY_COLORS = new HashMap<>();

    static {
        RARITY_COLORS.put("Common", R.color.rarityCommon);
        RARITY_COLORS.put("Uncommon", R.color.rarityUncommon);
        RARITY_COLORS.put("Rare", R.color.rarityRare);
        RARITY_COLORS.put("Mythic Rare", R.color.rarityMythicRare);
        RARITY_COLORS.put("Special", R.color.rarityMythicRare);
    }

    @NonNull
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

    public String manaCost;

    public String cmc;

    public String rarity;

    public String rulesText = "";

    public int count;

    @ColumnInfo(name = "card_id")
    public String parent;

    public boolean child;



    @Ignore
    public List<ForeignName> foreignNames;

    @Ignore
    public List<Rule> rulings;

    @Ignore
    public List<String> supertypes;

    @Ignore
    public List<String> types;

    @Ignore
    public List<String> subtypes;

    @Ignore
    public List<String> colors;

    @Ignore
    public List<String> printings;

    @Ignore
    public String nameOrigin;

    public Card(String id) {
        this.id = id;
    }

    public int getRarityColor() {
        return Card.getRarityColor(rarity);
    }

    static int getRarityColor(String rarity) {
        if (RARITY_COLORS.containsKey(rarity)) {
            return RARITY_COLORS.get(rarity);
        }
        return RARITY_COLORS.get("Common");
    }

    public int getSetIcon() {
        return Card.getSetIcon(set);
    }

    public static int getSetIcon(String set) {
        if (Icons.SET_ICONS.containsKey(set)) {
            return Icons.SET_ICONS.get(set);
        }
        return Icons.SET_ICONS.get("DEFAULT");
    }

    public void prepare() {
        if (nameOrigin == null) {
            nameOrigin = name;
            prepareNumber();
            prepareImageUrl();
            prepareRules();
        }
    }

    private void prepareRules() {
        //Правила
        if (rulings != null) {
            StringBuilder textRules = new StringBuilder();
            for (Card.Rule rule : rulings) {
                textRules.append(rule.text).append("\n\n");
            }
            rulesText = textRules.toString();
        }
    }

    private void prepareImageUrl() {
        //Локализованная картинка
        if (foreignNames != null) {
            for (Card.ForeignName foreignName : foreignNames) {
                if ("Russian".equals(foreignName.language)) {
                    imageUrl = foreignName.imageUrl;
                    name = foreignName.name;
                    break;
                }
            }
        }
    }

    private void prepareNumber() {
        numberFormatted = number;
        if(number == null){
            return;
        }
        if (number.endsWith("a") || number.endsWith("b")) {
            if (number.length() == 2) {
                numberFormatted = "00" + number;
            } else if (number.length() == 3) {
                numberFormatted = "0" + number;
            }
            numberFormatted = numberFormatted.replace("a", "").replace("b", "");
        } else if (number.length() == 1) {
            numberFormatted = "00" + number;
        } else if (number.length() == 2) {
            numberFormatted = "0" + number;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Card && other.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, imageUrl, text, flavor, numberFormatted,
                number, setName, set, type, manaCost, cmc, rarity, rulesText);
    }

    private static class ForeignName {
        public String name;
        public String language;
        public String imageUrl;
        public String multiverseid;
    }

    private static class Rule {
        public String text;
        public String date;
    }

}
