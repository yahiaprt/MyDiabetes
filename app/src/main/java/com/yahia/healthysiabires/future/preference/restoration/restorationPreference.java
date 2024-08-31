package com.yahia.healthysiabires.future.preference.restoration;

import android.content.Context;
import android.util.AttributeSet;

import com.yahia.healthysiabires.partage.event.permision.PermisionRequestEt;
import com.yahia.healthysiabires.partage.data.permission.Permission;
import com.yahia.healthysiabires.partage.data.permission.PermissionUC;

abstract class restorationPreference extends ClickablePreference<PermisionRequestEt> {

    restorationPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    abstract PermissionUC getUseCase();

    @Override
    public PermisionRequestEt getEvent() {
        return new PermisionRequestEt(Permission.WRITE_EXTERNAL_STORAGE, getUseCase());
    }
}