package ru.devsp.app.mtgcollections.view.adapters.holders

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_loading.view.*
import kotlinx.android.synthetic.main.list_item_spoiler.view.*
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.tools.ImageLoader

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

        ImageLoader.loadImageFromCache(cardImage, item.imageUrl)
        itemBlock.setOnClickListener(listener)
    }

    fun switchLoading(loading : Boolean) = with(itemView) {
        loadingBlock.visibility = if(loading) View.VISIBLE else View.INVISIBLE
    }
}