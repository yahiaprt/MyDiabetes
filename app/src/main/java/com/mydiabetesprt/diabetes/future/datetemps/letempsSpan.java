package com.mydiabetesprt.diabetes.future.datetemps;

import android.content.Context;

import androidx.annotation.StringRes;

import com.yahia.healthysiabires.R;import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

/**
 * Created by Faltenreich on 22.03.2016.
 */
public enum letempsSpan {

    WEEK(R.string.week, R.string.day, DateTimeConstants.DAYS_PER_WEEK),
    MONTH(R.string.month, R.string.calendarweek, 4),
    YEAR(R.string.year, R.string.month, 12);

    private int intervalStringResId;
    private int subIntervalStringResId;
    public int stepsPerInterval;

    letempsSpan(@StringRes int intervalStringResId, @StringRes int subIntervalStringResId, int stepsPerInterval) {
        this.intervalStringResId = intervalStringResId;
        this.subIntervalStringResId = subIntervalStringResId;
        this.stepsPerInterval = stepsPerInterval;
    }

    public String toIntervalLabel(Context context) {
        return context.getString(intervalStringResId);
    }

    public String toSubIntervalLabel(Context context) {
        return context.getString(subIntervalStringResId);
    }

    public Interval getInterval(DateTime dateTime, int add) {
        switch (this) {
            case WEEK:
                return new Interval(dateTime.plusWeeks(add), dateTime);
            case MONTH:
                return new Interval(dateTime.plusMonths(add), dateTime);
            case YEAR:
                return new Interval(dateTime.plusYears(add), dateTime);
            default:
                throw new IllegalArgumentException("Unknown enum value: " + this.toString());
        }
    }

    public DateTime getStep(DateTime dateTime, int add) {
        switch (this) {
            case WEEK:
                return dateTime.plusDays(add);
            case MONTH:
                return dateTime.plusWeeks(add);
            case YEAR:
                return dateTime.plusMonths(add);
            default:
                throw new IllegalArgumentException("Unknown enum value: " + this.toString());
        }
    }

    public String getLabel(DateTime dateTime) {
        switch (this) {
            case WEEK:
                return choisirletimeUS.toWeekDayShort(dateTime);
            case MONTH:
                return dateTime.toString("w");
            case YEAR:
                return dateTime.toString("MMM");
            default:
                return dateTime.toString();
        }
    }
}
