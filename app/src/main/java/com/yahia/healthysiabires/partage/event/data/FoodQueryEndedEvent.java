package com.yahia.healthysiabires.partage.event.data;

import com.yahia.healthysiabires.partage.event.BaseContextEt;

/**
 * Created by Faltenreich on 10.11.2016.
 */

public class FoodQueryEndedEvent extends BaseContextEt<Boolean> {

    public FoodQueryEndedEvent(Boolean hasMore) {
        super(hasMore);
    }
}
