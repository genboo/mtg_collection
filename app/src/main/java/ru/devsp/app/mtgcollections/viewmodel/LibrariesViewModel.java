package ru.devsp.app.mtgcollections.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.app.mtgcollections.model.objects.Library;
import ru.devsp.app.mtgcollections.model.objects.LibraryInfo;
import ru.devsp.app.mtgcollections.repository.LibrariesRepository;

/**
 *
 * Created by gen on 31.08.2017.
 */

public class LibrariesViewModel extends ViewModel {

    private LibrariesRepository mLibrariesRepository;
    private LiveData<List<LibraryInfo>> mAllLibraries;

    @Inject
    LibrariesViewModel(LibrariesRepository librariesRepository) {
        mLibrariesRepository = librariesRepository;
        mAllLibraries = mLibrariesRepository.getLibrariesInfoList();
    }


    public LiveData<List<LibraryInfo>> getAllLibraries(){
        return mAllLibraries;
    }

    public void add(Library item){
        mLibrariesRepository.save(item);
    }

}
