package com.yahia.healthysiabires.partage.event.ui;

import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;
import com.yahia.healthysiabires.partage.event.BaseContextEt;

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
