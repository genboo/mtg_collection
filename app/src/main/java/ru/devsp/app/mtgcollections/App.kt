package ru.devsp.app.mtgcollections

import android.app.Application
import com.crashlytics.android.Crashlytics
import ru.devsp.app.mtgcollections.di.components.AppComponent
import ru.devsp.app.mtgcollections.di.components.DaggerAppComponent
import io.fabric.sdk.android.Fabric

class App : Application() {

    val appComponent: AppComponent by lazy{
        DaggerAppComponent.builder().context(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }

}