package com.mydiabetesprt.diabetes.future.export.job.csv;

import android.content.Context;

import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.future.export.job.ExportCallback;
import com.mydiabetesprt.diabetes.future.export.job.ExportConfig;
import com.mydiabetesprt.diabetes.future.export.job.FileType;

import org.joda.time.DateTime;

public class CsvExportConfig extends ExportConfig {

    private boolean isBackup;

    public CsvExportConfig(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        type[] categories,
        boolean isBackup
    ) {
        super(context, callback, dateStart, dateEnd, categories, FileType.CSV);
        this.isBackup = isBackup;
    }

    boolean isBackup() {
        return isBackup;
    }
}
