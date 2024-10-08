package com.yahia.healthysiabires.future.preference.usin;

import android.content.Context;
import android.util.AttributeSet;

import com.yahia.healthysiabires.partage.data.database.entity.type;
import com.yahia.healthysiabires.partage.data.preference.PreferenceHelper;
import com.yahia.healthysiabires.partage.event.ets;
import com.yahia.healthysiabires.partage.event.preference.CorrectionusinChangedEt;

/**
 * Created by Filip on 04.11.13.
 */
public class CorrectionusinPreference extends usinPreference {

    public CorrectionusinPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setTimeInterval(TimeInterval interval) {
        PreferenceHelper.getInstance().setCorrectionInterval(interval);
    }

    @Override
    protected TimeInterval getTimeInterval() {
        return PreferenceHelper.getInstance().getCorrectionInterval();
    }

    @Override
    protected void storeValueForHour(float value, int hourOfDay) {
        value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(type.BLOODSUGAR, value);
        PreferenceHelper.getInstance().setCorrectionForHour(hourOfDay, value);
    }

    @Override
    protected float getValueForHour(int hourOfDay) {
        float value = PreferenceHelper.getInstance().getCorrectionForHour(hourOfDay);
        return PreferenceHelper.getInstance().formatDefaultToCustomUnit(type.BLOODSUGAR, value);
    }

    @Override
    protected void onPreferenceUpdate() {
        super.onPreferenceUpdate();
        ets.post(new CorrectionusinChangedEt());
    }
}