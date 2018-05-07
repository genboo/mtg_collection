package ru.devsp.app.mtgcollections.tools

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import ru.devsp.app.mtgcollections.R
import java.lang.Exception

object ImageLoader {
    fun loadImageFromCache(imageView: ImageView, image: String?) {
        //Принудительная загрузка из кэша
        Picasso.get()
                .load(image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.pic_card_back)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        //not used
                    }

                    override fun onError(ex: Exception) {
                        //Если в кэше все-таки нет, загружаем из сети
                        loadImage(imageView, image)
                    }
                })
    }

    fun loadImage(imageView: ImageView, image: String?) {
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.pic_card_back)
                .into(imageView)
    }

}