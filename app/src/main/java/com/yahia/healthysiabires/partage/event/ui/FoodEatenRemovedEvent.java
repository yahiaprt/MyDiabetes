package com.yahia.healthysiabires.partage.event.ui;

import com.yahia.healthysiabires.partage.data.database.entity.FoodEaten;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodEatenRemovedEvent extends FoodEatenListEvent {

    public FoodEatenRemovedEvent(FoodEaten context, int position) {
        super(context, position);
    }
}
