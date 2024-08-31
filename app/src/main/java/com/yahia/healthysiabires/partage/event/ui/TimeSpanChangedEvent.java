package com.yahia.healthysiabires.partage.event.ui;

import com.yahia.healthysiabires.partage.event.BaseContextEt;
import com.yahia.healthysiabires.future.datetemps.letempsSpan;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class TimeSpanChangedEvent extends BaseContextEt<letempsSpan> {

    public TimeSpanChangedEvent(letempsSpan timeSpan) {
        super(timeSpan);
    }
}
