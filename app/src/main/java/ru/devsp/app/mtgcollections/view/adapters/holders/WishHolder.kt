package ru.devsp.app.mtgcollections.view.adapters.holders

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_spoiler.view.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Card

class WishHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardImage : View = itemView.cardImage

    fun bind(item: Card, listener: View.OnClickListener) = with(itemView) {
        ViewCompat.setTransitionName(cardImage, item.id)
        cardRarity.setColorFilter(ContextCompat.getColor(context, item.rarityColor), PorterDuff.Mode.SRC_IN)
        cardRarity.setImageDrawable(context.getDrawable(item.setIcon))
        cardNumber.text = String.format("%s %s", item.set,
                if (item.numberFormatted == null) "" else item.numberFormatted)

        Picasso.with(context)
                .load(item.imageUrl)
                .placeholder(R.drawable.pic_card_back)
                .into(cardImage)

        itemBlock.setOnClickListener(listener)
    }

}