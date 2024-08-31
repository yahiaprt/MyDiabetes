package com.yahia.healthysiabires.future.log.jour;

import com.yahia.healthysiabires.future.log.LogListItem;

import org.joda.time.DateTime;

/**
 * Created by Filip on 10.07.2015.
 */
public class LogDayListItem extends LogListItem {

    public LogDayListItem(DateTime dateTime) {
        super(dateTime.withTimeAtStartOfDay());
    }
}
