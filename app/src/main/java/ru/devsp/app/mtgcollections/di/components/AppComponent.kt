package ru.devsp.app.mtgcollections.di.components


import android.content.Context

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import ru.devsp.app.mtgcollections.MainActivity
import ru.devsp.app.mtgcollections.di.ViewModelModule
import ru.devsp.app.mtgcollections.di.modules.DbModule
import ru.devsp.app.mtgcollections.di.modules.Prefs
import ru.devsp.app.mtgcollections.di.modules.RetrofitModule
import ru.devsp.app.mtgcollections.view.CardFragment
import ru.devsp.app.mtgcollections.view.CollectionFragment
import ru.devsp.app.mtgcollections.view.FullScreenImageFragment
import ru.devsp.app.mtgcollections.view.GalleryFragment
import ru.devsp.app.mtgcollections.view.LibrariesFragment
import ru.devsp.app.mtgcollections.view.LibraryFragment
import ru.devsp.app.mtgcollections.view.PlayersFragment
import ru.devsp.app.mtgcollections.view.SearchFragment
import ru.devsp.app.mtgcollections.view.SetsFragment
import ru.devsp.app.mtgcollections.view.SettingsFragment
import ru.devsp.app.mtgcollections.view.SpoilersFragment
import ru.devsp.app.mtgcollections.view.WishFragment

/**
 * Компонент di
 * Created by gen on 27.09.2017.
 */
@Component(modules = [ViewModelModule::class, RetrofitModule::class, DbModule::class, Prefs::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(activity: MainActivity)

    fun inject(fragment: SearchFragment)

    fun inject(fragment: CollectionFragment)

    fun inject(fragment: GalleryFragment)

    fun inject(fragment: CardFragment)

    fun inject(fragment: LibrariesFragment)

    fun inject(fragment: LibraryFragment)

    fun inject(fragment: FullScreenImageFragment)

    fun inject(fragment: PlayersFragment)

    fun inject(fragment: WishFragment)

    fun inject(fragment: SetsFragment)

    fun inject(fragment: SpoilersFragment)

    fun inject(fragment: SettingsFragment)

}