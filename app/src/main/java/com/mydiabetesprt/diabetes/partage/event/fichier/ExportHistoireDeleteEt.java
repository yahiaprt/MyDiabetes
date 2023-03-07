package com.mydiabetesprt.diabetes.partage.event.fichier;

import com.mydiabetesprt.diabetes.partage.event.BaseContextEt;
import com.mydiabetesprt.diabetes.future.export.histoire.ExportHistoryListItem;

public class ExportHistoireDeleteEt extends BaseContextEt<ExportHistoryListItem> {

    public ExportHistoireDeleteEt(ExportHistoryListItem item) {
        super(item);
    }
}
