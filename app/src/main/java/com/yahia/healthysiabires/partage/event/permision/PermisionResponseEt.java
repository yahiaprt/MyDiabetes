package com.yahia.healthysiabires.partage.event.permision;

import com.yahia.healthysiabires.partage.event.BaseContextEt;
import com.yahia.healthysiabires.partage.data.permission.Permission;
import com.yahia.healthysiabires.partage.data.permission.PermissionUC;

public class PermisionResponseEt extends BaseContextEt<Permission> {

    public PermissionUC useCase;
    public boolean isGranted;

    public PermisionResponseEt(Permission permission, PermissionUC useCase, boolean isGranted) {
        super(permission);
        this.useCase = useCase;
        this.isGranted = isGranted;
    }
}
