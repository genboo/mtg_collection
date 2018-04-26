package ru.devsp.app.mtgcollections.view.adapters.holders

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_spoiler.view.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Card

class SpoilerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Card, cards: HashMap<String, Int>, listener: View.OnClickListener) = with(itemView) {
        cardRarity.setColorFilter(ContextCompat.getColor(cardRarity.context, item.rarityColor), PorterDuff.Mode.SRC_IN)
        cardRarity.setImageDrawable(cardRarity.context.getDrawable(item.setIcon))
        cardNumber.text = String.format("%s %s", item.set, item.numberFormatted ?: "")

        if (cards.containsKey(item.number)) {
            cardExists.text = String.format("%s", cards[item.number])
            cardExists.visibility = View.VISIBLE
        } else {
            cardExists.text = ""
            cardExists.visibility = View.INVISIBLE
        }
        Picasso.with(context)
                .load(item.imageUrl)
                .placeholder(R.drawable.pic_card_back)
                .into(cardImage)

        itemBlock.setOnClickListener(listener)
    }

}