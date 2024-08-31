package com.yahia.healthysiabires.future.log.mois;

import com.yahia.healthysiabires.future.log.LogListItem;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class LogMonthListItem extends LogListItem {

    public LogMonthListItem(DateTime dateTime) {
        super(dateTime.withTimeAtStartOfDay());
    }
}
