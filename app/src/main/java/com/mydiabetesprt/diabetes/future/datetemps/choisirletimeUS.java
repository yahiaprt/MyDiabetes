package com.mydiabetesprt.diabetes.future.datetemps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.yahia.healthysiabires.R;import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

public class choisirletimeUS {

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static String toWeekDayShort(DateTime dateTime, boolean withDot) {
        String weekDay = dateTime.toString("E");
        if (!withDot && weekDay.length() >= 1) {
            weekDay = weekDay.substring(0, weekDay.length() - 1);
        }
        return weekDay;
    }

    static String toWeekDayShort(DateTime dateTime) {
        return toWeekDayShort(dateTime, false);
    }

    @SuppressLint("DefaultLocale")
    public static String parseInterval(Context context, long intervalInMillis) {
        if (intervalInMillis > (DateTimeConstants.MILLIS_PER_DAY * 2)) {
            return String.format("%d %s",
                    intervalInMillis / DateTimeConstants.MILLIS_PER_DAY,
                    context.getString(R.string.days));
        } else if (intervalInMillis > (DateTimeConstants.MILLIS_PER_HOUR * 2)) {
            return String.format("%d %s",
                    intervalInMillis / DateTimeConstants.MILLIS_PER_HOUR,
                    context.getString(R.string.hours));
        } else if (intervalInMillis > (DateTimeConstants.MILLIS_PER_MINUTE * 2)) {
            return String.format("%d %s",
                    intervalInMillis / DateTimeConstants.MILLIS_PER_MINUTE,
                    context.getString(R.string.minutes));
        } else {
            return String.format("1 %s",
                    context.getString(R.string.minute));
        }
    }

    public static DateTime parseFromString(String text, String format) {
        return TextUtils.isEmpty(text) && format.isEmpty() ?
                DateTimeFormat.forPattern(format).parseDateTime(text) :
                null;
    }

    public static String toWeekDayAndDate(DateTime dateTime) {
        String weekDayString = DateTimeFormat.forPattern("E").print(dateTime);
        String dateString = DateTimeFormat.shortDate().print(dateTime);
        return String.format("%s, %s", weekDayString, dateString);
    }

    public static DateTime withStartOfQuarter(DateTime dateTime) {
        int monthOfYear = dateTime.getMonthOfYear();
        DateTime firstMonthOfQuarter;
        if (monthOfYear >= DateTimeConstants.OCTOBER) {
            firstMonthOfQuarter = dateTime.withMonthOfYear(DateTimeConstants.OCTOBER);
        } else if (monthOfYear >= DateTimeConstants.JULY) {
            firstMonthOfQuarter = dateTime.withMonthOfYear(DateTimeConstants.JULY);
        } else if (monthOfYear >= DateTimeConstants.APRIL) {
            firstMonthOfQuarter = dateTime.withMonthOfYear(DateTimeConstants.APRIL);
        } else {
            firstMonthOfQuarter = dateTime.withMonthOfYear(DateTimeConstants.JANUARY);
        }
        return firstMonthOfQuarter.withDayOfMonth(1);
    }

    public static String toDateString(DateTime dateTime) {
        return DateTimeFormat.forPattern(FORMAT_DATE).print(dateTime);
    }
}
