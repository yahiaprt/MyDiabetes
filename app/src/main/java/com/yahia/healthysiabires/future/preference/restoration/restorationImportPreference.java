package com.yahia.healthysiabires.future.preference.restoration;

import android.content.Context;
import android.util.AttributeSet;

import com.yahia.healthysiabires.partage.data.permission.PermissionUC;

public class restorationImportPreference extends restorationPreference {

    public restorationImportPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    PermissionUC getUseCase() {
        return PermissionUC.BACKUP_READ;
    }
}