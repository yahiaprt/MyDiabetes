package com.yahia.healthysiabires.partage.event.preference;

import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.event.BaseContextEt;

/**
 * Created by Faltenreich on 04.05.2017
 */

public class UnitChangedEt extends BaseContextEt<type> {

    public UnitChangedEt(type context) {
        super(context);
    }
}
