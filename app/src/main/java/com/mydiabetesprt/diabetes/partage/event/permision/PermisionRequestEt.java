package com.mydiabetesprt.diabetes.partage.event.permision;

import com.mydiabetesprt.diabetes.partage.event.BaseContextEt;
import com.mydiabetesprt.diabetes.partage.data.permission.Permission;
import com.mydiabetesprt.diabetes.partage.data.permission.PermissionUC;

public class PermisionRequestEt extends BaseContextEt<Permission> {

    public PermissionUC useCase;

    public PermisionRequestEt(Permission permission, PermissionUC useCase) {
        super(permission);
        this.useCase = useCase;
    }
}
