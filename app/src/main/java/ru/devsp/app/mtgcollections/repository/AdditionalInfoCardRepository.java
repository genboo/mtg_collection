package ru.devsp.app.mtgcollections.repository;


import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.app.mtgcollections.model.db.AdditionalInfoCardDao;
import ru.devsp.app.mtgcollections.model.objects.Card;
import ru.devsp.app.mtgcollections.model.objects.Color;
import ru.devsp.app.mtgcollections.model.objects.Subtype;
import ru.devsp.app.mtgcollections.model.objects.Supertype;
import ru.devsp.app.mtgcollections.model.objects.Type;
import ru.devsp.app.mtgcollections.tools.AppExecutors;

/**
 * Обработка связей карт и колод
 * Created by gen on 03.10.2017.
 */
@Singleton
public class AdditionalInfoCardRepository {

    private final AppExecutors mAppExecutors;

    private AdditionalInfoCardDao mDao;

    @Inject
    AdditionalInfoCardRepository(AppExecutors appExecutors, AdditionalInfoCardDao dao) {
        mAppExecutors = appExecutors;
        mDao = dao;
    }

    public void save(Card item) {
        mAppExecutors.diskIO().execute(() -> updateInfo(item));
    }

    private void updateInfo(Card item) {
        if (item.colors != null) {
            mDao.clearColor(item.id);
            for (String str : item.colors) {
                Color c = new Color();
                c.cardId = item.id;
                c.color = str;
                mDao.insert(c);
            }
        }
        if (item.types != null) {
            mDao.clearType(item.id);
            for (String str : item.types) {
                Type t = new Type();
                t.cardId = item.id;
                t.type = str;
                mDao.insert(t);
            }
        }
        if (item.supertypes != null) {
            mDao.clearSupertype(item.id);
            for (String str : item.supertypes) {
                Supertype supt = new Supertype();
                supt.cardId = item.id;
                supt.supertype = str;
                mDao.insert(supt);
            }
        }
        if (item.subtypes != null) {
            mDao.clearSubtype(item.id);
            for (String str : item.subtypes) {
                Subtype subt = new Subtype();
                subt.cardId = item.id;
                subt.subtype = str;
                mDao.insert(subt);
            }
        }
    }
}
