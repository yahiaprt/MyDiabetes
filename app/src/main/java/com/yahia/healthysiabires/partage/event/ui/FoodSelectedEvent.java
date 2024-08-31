package com.yahia.healthysiabires.partage.event.ui;

import android.view.View;

import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.event.BaseContextEt;

/**
 * Created by Faltenreich on 08.10.2016.
 */

public class FoodSelectedEvent extends BaseContextEt<Food> {

    public View view;

    public FoodSelectedEvent(Food context, View view) {
        super(context);
        this.view = view;
    }
}
