package com.mydiabetesprt.diabetes.partage.event.ui;

import com.mydiabetesprt.diabetes.partage.data.database.entity.FoodEaten;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodEatenUpdatedEvent extends FoodEatenListEvent {

    public FoodEatenUpdatedEvent(FoodEaten context, int position) {
        super(context, position);
    }
}
