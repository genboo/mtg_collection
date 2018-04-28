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
import java.util.*

class CardLibraryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardImage: View? = itemView.cardImage

    fun bind(item: CardListItem, listener: View.OnClickListener) = with(itemView) {
        if (item.card == null) {
            headerName.text = item.title
        } else {
            cardName.text = item.card.name
            cardRarity.setColorFilter(ContextCompat.getColor(cardRarity.context, item.card.rarityColor), PorterDuff.Mode.SRC_IN)
            cardRarity.setImageDrawable(cardRarity.context.getDrawable(item.card.setIcon))
            cardSet.text = item.card.setName
            cardType.text = item.card.type
            cardCount.text = String.format(Locale.getDefault(), "Кол-во: %d", item.card.count)
            cardNumber.text = String.format(Locale.getDefault(), "%s %s", item.card.set, item.card.numberFormatted)

            ViewCompat.setTransitionName(cardImage, item.card.id)
            Picasso.with(context)
                    .load(item.card.imageUrl)
                    .placeholder(R.drawable.pic_card_back)
                    .into(cardImage)

            itemBlock.setOnClickListener(listener)
        }
    }

}