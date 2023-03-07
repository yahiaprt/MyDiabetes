package com.mydiabetesprt.diabetes.partage.event.ui;

import android.view.View;

import com.mydiabetesprt.diabetes.partage.data.database.entity.Food;
import com.mydiabetesprt.diabetes.partage.event.BaseContextEt;

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
