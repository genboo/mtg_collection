package ru.devsp.app.mtgcollections.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.repository.CardLocalRepository

/**
 * Коллекция карт
 * Created by gen on 31.08.2017.
 */

class WishViewModel @Inject
internal constructor(cardLocalRepository: CardLocalRepository) : ViewModel() {
    val wishList: LiveData<List<Card>> = cardLocalRepository.wishList
}
