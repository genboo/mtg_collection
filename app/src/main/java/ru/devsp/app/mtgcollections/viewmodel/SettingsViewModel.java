package ru.devsp.app.mtgcollections.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.inject.Inject;

import ru.devsp.app.mtgcollections.di.modules.DbModule;
import ru.devsp.app.mtgcollections.tools.Logger;


/**
 * Настройки
 * Created by gen on 11.01.2018.
 */

public class SettingsViewModel extends ViewModel {

    private String path = Environment.getExternalStorageDirectory().getPath() + "/data/";

    @Inject
    SettingsViewModel() {

    }

    public boolean backup(Context context) {
        String fullBackupDir = path + context.getPackageName();
        File dbFile = context.getDatabasePath(DbModule.DB_NAME);
        if (dbFile.exists()) {
            try {
                File dir = new File(fullBackupDir);
                if (!dir.exists() && !dir.mkdirs()) {
                    Logger.INSTANCE.e("Каталоги не созданы");
                }

                File backup = new File(fullBackupDir, DbModule.DB_NAME);
                if (!backup.exists() && !backup.createNewFile()) {
                    Logger.INSTANCE.e("Копия не создана");
                }
                try (FileInputStream fileInputStream = new FileInputStream(dbFile)) {
                    FileChannel src = fileInputStream.getChannel();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(backup)) {
                        FileChannel dst = fileOutputStream.getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        return true;
                    }
                }
            } catch (IOException ex) {
                Logger.INSTANCE.e(ex);
            }
        }
        return false;
    }

    public boolean restore(Context context) {
        String fullBackupDir = path + context.getPackageName();
        File backup = new File(fullBackupDir, DbModule.DB_NAME);
        if (backup.exists()) {
            try {
                File dbFile = context.getDatabasePath(DbModule.DB_NAME);
                if (!dbFile.exists() && !dbFile.createNewFile()) {
                    Logger.INSTANCE.e("Локальная БД не создана");
                }
                try (FileInputStream fileInputStream = new FileInputStream(backup)) {
                    FileChannel src = fileInputStream.getChannel();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(dbFile)) {
                        FileChannel dst = fileOutputStream.getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        return true;
                    }
                }
            } catch (IOException ex) {
                Logger.INSTANCE.e(ex);
            }
        }
        return false;
    }

}
