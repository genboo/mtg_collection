package ru.devsp.app.mtgcollections.di.components;

import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import ru.devsp.app.mtgcollections.MainActivity;
import ru.devsp.app.mtgcollections.di.modules.DbModule;
import ru.devsp.app.mtgcollections.di.modules.RetrofitModule;
import ru.devsp.app.mtgcollections.di.modules.ViewModelModule;
import ru.devsp.app.mtgcollections.view.CardFragment;
import ru.devsp.app.mtgcollections.view.CollectionFragment;
import ru.devsp.app.mtgcollections.view.FullScreenImageFragment;
import ru.devsp.app.mtgcollections.view.GalleryFragment;
import ru.devsp.app.mtgcollections.view.LibrariesFragment;
import ru.devsp.app.mtgcollections.view.LibraryFragment;
import ru.devsp.app.mtgcollections.view.PlayersFragment;
import ru.devsp.app.mtgcollections.view.SearchFragment;
import ru.devsp.app.mtgcollections.view.SetsFragment;
import ru.devsp.app.mtgcollections.view.SettingsFragment;
import ru.devsp.app.mtgcollections.view.SpoilersFragment;
import ru.devsp.app.mtgcollections.view.WishFragment;

/**
 * Компонент di
 * Created by gen on 27.09.2017.
 */
@Component(modules = {
        ViewModelModule.class,
        RetrofitModule.class,
        DbModule.class
})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        AppComponent build();
    }

    void inject(MainActivity activity);

    void inject(SearchFragment fragment);

    void inject(CollectionFragment fragment);

    void inject(GalleryFragment fragment);

    void inject(CardFragment fragment);

    void inject(LibrariesFragment fragment);

    void inject(LibraryFragment fragment);

    void inject(FullScreenImageFragment fragment);

    void inject(PlayersFragment fragment);

    void inject(WishFragment fragment);

    void inject(SetsFragment fragment);

    void inject(SpoilersFragment fragment);

    void inject(SettingsFragment fragment);

}
