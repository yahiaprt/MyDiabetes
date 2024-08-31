package com.yahia.healthysiabires.future.preference.restoration;

import android.content.Context;
import android.util.AttributeSet;

import com.yahia.healthysiabires.partage.data.permission.PermissionUC;

public class restorationExportPreference extends restorationPreference {

    public restorationExportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    PermissionUC getUseCase() {
        return PermissionUC.BACKUP_WRITE;
    }
}