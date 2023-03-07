package com.mydiabetesprt.diabetes.partage.event.ui;

import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;
import com.mydiabetesprt.diabetes.partage.event.BaseContextEt;

/**
 * Created by Faltenreich on 09.10.2016.
 */

public class FoodEatenListEvent extends BaseContextEt<FoodEaten> {

    public int position;

    public FoodEatenListEvent(FoodEaten context, int position) {
        super(context);
        this.position = position;
    }
}
