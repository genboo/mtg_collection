package ru.devsp.app.mtgcollections.tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import ru.devsp.app.mtgcollections.view.BaseFragment;

/**
 * Управление разрешениями приложения
 * Created by gen on 11.01.2018.
 */

public class PermissionsHelper {
    public static final int PERMISSION_REQUEST_CODE_STORAGE = 1;

    private PermissionsHelper(){
        //empty constructor
    }

    public static boolean havePermissionStorage(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermissions(BaseFragment fragment) {
        fragment.requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE_STORAGE);
    }
}
