package ru.devsp.app.mtgcollections.viewmodel


import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

import javax.inject.Inject

import ru.devsp.app.mtgcollections.di.modules.DbModule
import ru.devsp.app.mtgcollections.di.modules.Prefs
import ru.devsp.app.mtgcollections.tools.Logger


/**
 * Настройки
 * Created by gen on 11.01.2018.
 */

class SettingsViewModel @Inject
internal constructor(private val prefs: SharedPreferences) : ViewModel() {

    private val path = Environment.getExternalStorageDirectory().path + "/data/"

    fun backup(context: Context): Boolean {
        val fullBackupDir = path + context.packageName
        val dbFile = context.getDatabasePath(DbModule.DB_NAME)
        if (dbFile.exists()) {
            try {
                val dir = File(fullBackupDir)
                if (!dir.exists() && !dir.mkdirs()) {
                    Logger.e("Каталоги не созданы")
                }

                val backup = File(fullBackupDir, DbModule.DB_NAME)
                if (!backup.exists() && !backup.createNewFile()) {
                    Logger.e("Копия не создана")
                }
                FileInputStream(dbFile).use { fileInputStream ->
                    val src = fileInputStream.channel
                    FileOutputStream(backup).use { fileOutputStream ->
                        val dst = fileOutputStream.channel
                        dst.transferFrom(src, 0, src.size())
                        src.close()
                        dst.close()
                        return true
                    }
                }
            } catch (ex: IOException) {
                Logger.e(ex)
            }

        }
        return false
    }

    fun restore(context: Context): Boolean {
        val fullBackupDir = path + context.packageName
        val backup = File(fullBackupDir, DbModule.DB_NAME)
        if (backup.exists()) {
            try {
                val dbFile = context.getDatabasePath(DbModule.DB_NAME)
                if (!dbFile.exists() && !dbFile.createNewFile()) {
                    Logger.e("Локальная БД не создана")
                }
                FileInputStream(backup).use { fileInputStream ->
                    val src = fileInputStream.channel
                    FileOutputStream(dbFile).use { fileOutputStream ->
                        val dst = fileOutputStream.channel
                        dst.transferFrom(src, 0, src.size())
                        src.close()
                        dst.close()
                        return true
                    }
                }
            } catch (ex: IOException) {
                Logger.e(ex)
            }

        }
        return false
    }

    fun clearExpire() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

}
