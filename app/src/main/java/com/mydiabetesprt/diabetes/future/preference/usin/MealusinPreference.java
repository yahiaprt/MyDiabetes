package com.mydiabetesprt.diabetes.future.preference.usin;

import android.content.Context;
import android.util.AttributeSet;

import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.event.ets;
import com.mydiabetesprt.diabetes.partage.event.preference.MealusinChangedEt;

/**
 * Created by Filip on 04.11.13.
 */
public class MealusinPreference extends usinPreference {

    public MealusinPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setTimeInterval(TimeInterval interval) {
        PreferenceHelper.getInstance().setFactorInterval(interval);
    }

    @Override
    protected TimeInterval getTimeInterval() {
        return PreferenceHelper.getInstance().getFactorInterval();
    }

    @Override
    protected void storeValueForHour(float value, int hourOfDay) {
        PreferenceHelper.getInstance().setFactorForHour(hourOfDay, value);
    }

    @Override
    protected float getValueForHour(int hourOfDay) {
        return PreferenceHelper.getInstance().getFactorForHour(hourOfDay);
    }

    @Override
    protected void onPreferenceUpdate() {
        super.onPreferenceUpdate();
        ets.post(new MealusinChangedEt());
    }
}