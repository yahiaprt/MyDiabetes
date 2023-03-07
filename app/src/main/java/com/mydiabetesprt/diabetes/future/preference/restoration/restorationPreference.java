package com.mydiabetesprt.diabetes.future.preference.restoration;

import android.content.Context;
import android.util.AttributeSet;

import com.mydiabetesprt.diabetes.partage.event.permision.PermisionRequestEt;
import com.mydiabetesprt.diabetes.partage.data.permission.Permission;
import com.mydiabetesprt.diabetes.partage.data.permission.PermissionUC;

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