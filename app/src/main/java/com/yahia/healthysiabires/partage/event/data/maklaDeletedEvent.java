package com.yahia.healthysiabires.partage.event.data;

import com.yahia.healthysiabires.partage.data.database.entity.Food;
import com.yahia.healthysiabires.partage.event.BaseContextEt;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class maklaDeletedEvent extends BaseContextEt<Food> {

    public maklaDeletedEvent(Food makla) {
        super(makla);
    }
}
