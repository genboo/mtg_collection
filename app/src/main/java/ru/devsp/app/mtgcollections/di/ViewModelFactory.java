package ru.devsp.app.mtgcollections.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 *
 * Created by gen on 12.09.2017.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> mViewModels;

    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>,
            Provider<ViewModel>> viewModels) {
        this.mViewModels = viewModels;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Provider<ViewModel> viewModelProvider = mViewModels.get(modelClass);

        if (viewModelProvider == null) {
            throw new IllegalArgumentException("model class "
                    + modelClass
                    + " not found");
        }

        return (T) viewModelProvider.get();
    }
}
