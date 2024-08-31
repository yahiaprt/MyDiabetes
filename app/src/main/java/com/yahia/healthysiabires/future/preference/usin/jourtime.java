package com.yahia.healthysiabires.future.preference.usin;

import androidx.annotation.StringRes;

import com.yahia.healthysiabires.R;/**
 * Created by Faltenreich on 06.09.2016.
 */
public enum jourtime {

    MORNING(4, R.string.morning),
    NOON(10, R.string.noon),
    EVENING(16, R.string.evening),
    NIGHT(22, R.string.night);

    public static final int INTERVAL_LENGTH = 6;

    public int startingHour;
    public @StringRes int textResourceId;

    jourtime(int startingHour, @StringRes int textResourceId) {
        this.startingHour = startingHour;
        this.textResourceId = textResourceId;
    }

    public static jourtime toDayTime(int hourOfDay) {
        if (hourOfDay >=4 && hourOfDay < 10) {
            return MORNING;
        } else if (hourOfDay >= 10 && hourOfDay < 16) {
            return NOON;
        } else if (hourOfDay >= 16 && hourOfDay < 22) {
            return EVENING;
        } else {
            return NIGHT;
        }
    }

    public String toDeprecatedString() {
        switch (this) {
            case MORNING: return "Morning";
            case NOON: return "Noon";
            case EVENING: return "Evening";
            case NIGHT: return "Night";
            default: return null;
        }
    }
}
