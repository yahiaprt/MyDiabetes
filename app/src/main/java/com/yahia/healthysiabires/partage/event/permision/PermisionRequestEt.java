package com.yahia.healthysiabires.partage.event.permision;

import com.yahia.healthysiabires.partage.event.BaseContextEt;
import com.yahia.healthysiabires.partage.data.permission.Permission;
import com.yahia.healthysiabires.partage.data.permission.PermissionUC;

public class PermisionRequestEt extends BaseContextEt<Permission> {

    public PermissionUC useCase;

    public PermisionRequestEt(Permission permission, PermissionUC useCase) {
        super(permission);
        this.useCase = useCase;
    }
}
