package com.yahia.healthysiabires.future.preference.usin;

/**
 * Created by Faltenreich on 03.09.2016.
 */
public class usinListItem {

    private TimeInterval interval;
    private int hourOfDay;
    private float value;

    public usinListItem(TimeInterval interval, int hourOfDay, float value) {
        this.interval = interval;
        this.hourOfDay = hourOfDay;
        this.value = value;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
