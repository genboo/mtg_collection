package ru.devsp.app.mtgcollections

import android.arch.lifecycle.ViewModelProvider
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import ru.devsp.app.mtgcollections.di.components.AppComponent
import ru.devsp.app.mtgcollections.view.components.Navigation
import javax.inject.Inject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var navigation: Navigation

    private var component: AppComponent? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //Хак, инициализируем настройки тут, чтобы потом первое обращение не вызывало фризов
    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setBackgroundDrawableResource(R.drawable.transparent)
        if (component == null) {
            component = (application as App).appComponent
            component?.inject(this)
        }

        supportFragmentManager.addOnBackStackChangedListener{ this.updateToolbarIcon() }

        navigation = Navigation(supportFragmentManager)

        toggle = ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        navigationView.itemIconTintList = null
        navigationView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            navigation.toFirst()
            navigationView.menu.getItem(2).isChecked = true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_search -> navigation.toSearch()
            R.id.nav_collection -> navigation.toCollection()
            R.id.nav_libraries -> navigation.toLibraries()
            R.id.nav_game -> navigation.toPlayers()
            R.id.nav_wish_list -> navigation.toWishList()
            R.id.nav_sets -> navigation.toSets()
            R.id.nav_settings -> navigation.toSettings()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun updateDrawer(toolbar: Toolbar) {
        drawerLayout.removeDrawerListener(toggle)
        toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.setToolbarNavigationClickListener{ _ -> onBackPressed() }
        toggle.syncState()
    }

    fun updateActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateToolbarIcon() {
        if (supportActionBar != null) {
            toggle.isDrawerIndicatorEnabled = supportFragmentManager.backStackEntryCount == 0
        }
    }

}