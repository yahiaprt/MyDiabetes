package com.mydiabetesprt.diabetes.partage.event.data;

import com.mydiabetesprt.diabetes.partage.data.database.entity.BaseEntite;
import com.mydiabetesprt.diabetes.partage.event.BaseContextEt;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public abstract class BaseDataEt<T extends BaseEntite> extends BaseContextEt<T> {

    public BaseDataEt(T entity) {
        super(entity);
    }
}
