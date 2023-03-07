package com.mydiabetesprt.diabetes.partage.event.ui;

import com.mydiabetesprt.diabetes.partage.event.BaseContextEt;
import com.mydiabetesprt.diabetes.future.datetemps.letempsSpan;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class TimeSpanChangedEvent extends BaseContextEt<letempsSpan> {

    public TimeSpanChangedEvent(letempsSpan timeSpan) {
        super(timeSpan);
    }
}
