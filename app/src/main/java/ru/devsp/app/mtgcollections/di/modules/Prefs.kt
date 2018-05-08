package ru.devsp.app.mtgcollections.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class Prefs {

    @Provides
    @Singleton
    fun providePrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("main", Context.MODE_PRIVATE)
    }

    companion object {
        const val SETS_TIME = "sets_time"
    }

}