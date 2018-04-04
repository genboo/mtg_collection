package ru.devsp.app.mtgcollections.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ru.devsp.app.mtgcollections.di.ViewModelFactory;
import ru.devsp.app.mtgcollections.viewmodel.CardViewModel;
import ru.devsp.app.mtgcollections.viewmodel.CollectionViewModel;
import ru.devsp.app.mtgcollections.viewmodel.GameViewModel;
import ru.devsp.app.mtgcollections.viewmodel.LibrariesViewModel;
import ru.devsp.app.mtgcollections.viewmodel.SearchViewModel;
import ru.devsp.app.mtgcollections.viewmodel.SetsViewModel;
import ru.devsp.app.mtgcollections.viewmodel.SettingsViewModel;
import ru.devsp.app.mtgcollections.viewmodel.SpoilersViewModel;
import ru.devsp.app.mtgcollections.viewmodel.WishViewModel;

/**
 * ViewModel
 * Created by gen on 12.09.2017.
 */

@SuppressWarnings("unused")
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LibrariesViewModel.class)
    abstract ViewModel bindLibrariesViewModel(LibrariesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CollectionViewModel.class)
    abstract ViewModel bindCollectionViewModel(CollectionViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CardViewModel.class)
    abstract ViewModel bindCardViewModel(CardViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel.class)
    abstract ViewModel bindGameViewModel(GameViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WishViewModel.class)
    abstract ViewModel bindWishViewModel(WishViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SetsViewModel.class)
    abstract ViewModel bindSetsViewModel(SetsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SpoilersViewModel.class)
    abstract ViewModel bindSpoilersViewModel(SpoilersViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel.class)
    abstract ViewModel bindSettingsViewModel(SettingsViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}