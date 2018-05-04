package ru.devsp.app.mtgcollections.tools

import android.content.Context
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import ru.devsp.app.mtgcollections.R

object ImageLoader {
    fun loadImageFromCache(context: Context, imageView: ImageView, image: String) {
        val picasso = Picasso.with(context)
        //Принудительная загрузка из кэша
        picasso.load(image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.pic_card_back)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        //not used
                    }

                    override fun onError() {
                        //Если в кэше все-таки нет, загружаем из сети
                        loadImage(context, imageView, image)
                    }
                })
    }


    fun loadImage(context: Context, imageView: ImageView, image: String) {
        val picasso = Picasso.with(context)
        picasso.load(image)
                .placeholder(R.drawable.pic_card_back)
                .into(imageView)
    }

    fun loadImageFromCache(context: Context, fragment:Fragment, imageView: ImageView, image: String){
        val picasso = Picasso.with(context)
        //Принудительная загрузка из кэша
        picasso.load(image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.pic_card_back)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        fragment.startPostponedEnterTransition()
                    }

                    override fun onError() {
                        //Если в кэше все-таки нет, загружаем из сети
                        loadImage(context, fragment, imageView, image)
                    }
                })
    }

    fun loadImage(context: Context, fragment:Fragment, imageView: ImageView, image: String) {
        val picasso = Picasso.with(context)
        picasso.load(image)
                .placeholder(R.drawable.pic_card_back)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        fragment.startPostponedEnterTransition()
                    }

                    override fun onError() {
                        fragment.startPostponedEnterTransition()
                    }
                })
    }

}