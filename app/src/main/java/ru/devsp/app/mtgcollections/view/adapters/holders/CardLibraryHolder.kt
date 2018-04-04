package ru.devsp.app.mtgcollections.view.adapters.holders

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_card_header.view.*
import kotlinx.android.synthetic.main.list_item_card.view.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.view.adapters.CardListItem
import ru.devsp.app.mtgcollections.view.adapters.RecyclerViewAdapter
import java.util.*

class CardLibraryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: CardListItem, listener: RecyclerViewAdapter.OnItemClickListener<CardListItem>?) = with(itemView) {
        if (item.card == null) {
            headerName.text = item.title
        } else {
            cardName.text = item.card.name
            cardRarity.setColorFilter(ContextCompat.getColor(cardRarity.context, item.card.rarityColor), PorterDuff.Mode.SRC_IN)
            cardRarity.setImageDrawable(cardRarity.context.getDrawable(item.card.getSetIcon()))
            cardSet.text = item.card.setName
            cardType.text = item.card.type
            cardCount.text = String.format(Locale.getDefault(), "Кол-во: %d", item.card.count)
            cardNumber.text = String.format(Locale.getDefault(), "%s %s", item.card.set, item.card.numberFormatted)
            Picasso.with(context)
                    .load(item.card.imageUrl)
                    .placeholder(R.drawable.pic_card_back)
                    .into(cardImage)

            ViewCompat.setTransitionName(cardImage, item.card.id)

            itemBlock.setOnClickListener({ _ ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.click(adapterPosition, item, cardImage)
                }
            })
        }
    }

}