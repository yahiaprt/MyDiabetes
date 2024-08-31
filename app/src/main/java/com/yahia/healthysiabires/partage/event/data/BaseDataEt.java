package com.yahia.healthysiabires.partage.event.data;

import com.yahia.healthysiabires.partage.data.database.entity.BaseEntite;
import com.yahia.healthysiabires.partage.event.BaseContextEt;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public abstract class BaseDataEt<T extends BaseEntite> extends BaseContextEt<T> {

    public BaseDataEt(T entity) {
        super(entity);
    }
}
