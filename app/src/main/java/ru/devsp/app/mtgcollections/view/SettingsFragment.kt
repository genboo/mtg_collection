package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.tools.PermissionsHelper
import ru.devsp.app.mtgcollections.viewmodel.SettingsViewModel
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        initFragment()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)

        saveBackup.setOnClickListener { _ -> saveBackup(viewModel) }
        restoreBackup.setOnClickListener { _ -> restoreBackup(viewModel) }

        if (!PermissionsHelper.havePermissionStorage(context)) {
            PermissionsHelper.requestLocationPermissions(this)
            saveBackup.isEnabled = false
            restoreBackup.isEnabled = false
        }

    }

    private fun saveBackup(viewModel: SettingsViewModel) {
        if (viewModel.backup(context)) {
            showToast("Забекаплено")
        }
    }

    private fun restoreBackup(viewModel: SettingsViewModel) {
        if (viewModel.restore(context)) {
            showToast("Восстановлено")
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionsHelper.PERMISSION_REQUEST_CODE_STORAGE && PermissionsHelper.havePermissionStorage(context)) {
            saveBackup.isEnabled = true
            restoreBackup.isEnabled = true
        }
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return "Настройки"
    }

}
