package com.mydiabetesprt.diabetes.partage.event.data;

import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.event.BaseContextEt;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class maklaSavedEt extends BaseContextEt<Food> {

    public maklaSavedEt(Food makla) {
        super(makla);
    }
}
