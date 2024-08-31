package com.yahia.healthysiabires.partage.data.permission;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionMn {

    private static PermissionMn instance;

    public static PermissionMn getInstance() {
        if (instance == null) {
            instance = new PermissionMn();
        }
        return instance;
    }

    public boolean hasPermission(Activity activity, Permission permission) {
        return ContextCompat.checkSelfPermission(activity, permission.code) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(Activity activity, Permission permission, PermissionUC useCase) {
        requestPermission(activity, new Permission[]{permission}, useCase);
    }

    public void requestPermission(Activity activity, Permission[] permissions, PermissionUC useCase) {
        String[] permissionStrings = new String[permissions.length];
        for (int index = 0; index < permissions.length; index++) {
            permissionStrings[index] = permissions[index].code;
        }
        ActivityCompat.requestPermissions(activity, permissionStrings, useCase.requestCode);
    }
}
