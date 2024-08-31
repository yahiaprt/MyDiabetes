package com.yahia.healthysiabires.partage.event.networkage;

import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.event.BaseContextEt;

import java.util.List;

/**
 * Created by Faltenreich on 25.09.2016.
 */

public class maklaSearchSucceededEvent extends BaseContextEt<List<Food>> {

    public maklaSearchSucceededEvent(List<Food> context) {
        super(context);
    }
}
