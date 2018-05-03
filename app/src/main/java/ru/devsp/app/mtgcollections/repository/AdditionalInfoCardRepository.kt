package ru.devsp.app.mtgcollections.repository


import javax.inject.Inject
import javax.inject.Singleton

import ru.devsp.app.mtgcollections.model.db.AdditionalInfoCardDao
import ru.devsp.app.mtgcollections.model.objects.Card
import ru.devsp.app.mtgcollections.model.objects.Color
import ru.devsp.app.mtgcollections.model.objects.Subtype
import ru.devsp.app.mtgcollections.model.objects.Supertype
import ru.devsp.app.mtgcollections.model.objects.Type
import ru.devsp.app.mtgcollections.tools.AppExecutors

/**
 * Обработка связей карт и колод
 * Created by gen on 03.10.2017.
 */
@Singleton
class AdditionalInfoCardRepository @Inject
internal constructor(private val appExecutors: AppExecutors, private val dao: AdditionalInfoCardDao) {

    fun save(item: Card) {
        appExecutors.diskIO().execute { updateInfo(item) }
    }

    private fun updateInfo(item: Card) {
        if (item.colors != null) {
            dao.clearColor(item.id)
            for (str in item.colors) {
                val c = Color()
                c.cardId = item.id
                c.color = str
                dao.insert(c)
            }
        }
        if (item.types != null) {
            dao.clearType(item.id)
            for (str in item.types) {
                val t = Type()
                t.cardId = item.id
                t.type = str
                dao.insert(t)
            }
        }
        if (item.supertypes != null) {
            dao.clearSupertype(item.id)
            for (str in item.supertypes) {
                val supt = Supertype()
                supt.cardId = item.id
                supt.supertype = str
                dao.insert(supt)
            }
        }
        if (item.subtypes != null) {
            dao.clearSubtype(item.id)
            for (str in item.subtypes) {
                val subt = Subtype()
                subt.cardId = item.id
                subt.subtype = str
                dao.insert(subt)
            }
        }
    }
}
