package ru.devsp.app.mtgcollections.view.adapters.holders

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_spoiler.view.*
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.tools.ImageLoader

class WishHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardImage : View = itemView.cardImage

    fun bind(item: Card, listener: View.OnClickListener) = with(itemView) {
        ViewCompat.setTransitionName(cardImage, item.id)
        cardRarity.setColorFilter(ContextCompat.getColor(context, item.rarityColor), PorterDuff.Mode.SRC_IN)
        cardRarity.setImageDrawable(context.getDrawable(item.setIcon))
        cardNumber.text = String.format("%s %s", item.set,
                if (item.numberFormatted == null) "" else item.numberFormatted)

        if (item.count == 0) {
            cardExists.text = ""
            cardExists.visibility = View.INVISIBLE
        } else {
            cardExists.text = String.format("%s", item.count)
            cardExists.visibility = View.VISIBLE
        }

        ImageLoader.loadImageFromCache(cardImage, item.imageUrl)

        itemBlock.setOnClickListener(listener)
    }

}