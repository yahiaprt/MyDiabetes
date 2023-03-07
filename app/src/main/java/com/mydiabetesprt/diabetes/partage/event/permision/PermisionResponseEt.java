package com.mydiabetesprt.diabetes.partage.event.permision;

import com.mydiabetesprt.diabetes.partage.event.BaseContextEt;
import com.mydiabetesprt.diabetes.partage.data.permission.Permission;
import com.mydiabetesprt.diabetes.partage.data.permission.PermissionUC;

public class PermisionResponseEt extends BaseContextEt<Permission> {

    public PermissionUC useCase;
    public boolean isGranted;

    public PermisionResponseEt(Permission permission, PermissionUC useCase, boolean isGranted) {
        super(permission);
        this.useCase = useCase;
        this.isGranted = isGranted;
    }
}
