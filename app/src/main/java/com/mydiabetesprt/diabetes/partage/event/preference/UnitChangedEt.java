package com.mydiabetesprt.diabetes.partage.event.preference;

import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.partage.event.BaseContextEt;

/**
 * Created by Faltenreich on 04.05.2017
 */

public class UnitChangedEt extends BaseContextEt<type> {

    public UnitChangedEt(type context) {
        super(context);
    }
}
