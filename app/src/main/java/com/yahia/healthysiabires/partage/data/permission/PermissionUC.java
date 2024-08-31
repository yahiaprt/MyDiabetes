package com.yahia.healthysiabires.partage.data.permission;

import androidx.annotation.Nullable;

public enum PermissionUC {
    EXPORT,
    EXPORT_HISTORY,
    EXPORT_DELETE,
    BACKUP_WRITE,
    BACKUP_READ;

    public int requestCode = ordinal() + 123;

    @Nullable
    public static PermissionUC fromRequestCode(int requestCode) {
        for (PermissionUC useCase : values()) {
            if (useCase.requestCode == requestCode) {
                return useCase;
            }
        }
        return null;
    }
}