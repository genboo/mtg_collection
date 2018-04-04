package ru.devsp.app.mtgcollections.view.adapters;

import ru.devsp.app.mtgcollections.model.objects.CardForLibrary;

/**
 *
 * Created by gen on 01.11.2017.
 */

public class CardListItem {

    public final String title;

    public final CardForLibrary card;

    public CardListItem(String title, CardForLibrary card){
        this.title = title;
        this.card = card;
    }
}
