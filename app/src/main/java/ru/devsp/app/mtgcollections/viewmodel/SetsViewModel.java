package ru.devsp.app.mtgcollections.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.app.mtgcollections.model.tools.Resource;
import ru.devsp.app.mtgcollections.model.objects.Set;
import ru.devsp.app.mtgcollections.repository.SetsRepository;

/**
 * Список сетов
 * Created by gen on 22.12.2017.
 */

public class SetsViewModel extends ViewModel {

    private SetsRepository mSetsRepository;

    @Inject
    SetsViewModel(SetsRepository setsRepository) {
        mSetsRepository = setsRepository;
    }

    public LiveData<Resource<List<Set>>> getSets(){
        return mSetsRepository.getSets();
    }

}
