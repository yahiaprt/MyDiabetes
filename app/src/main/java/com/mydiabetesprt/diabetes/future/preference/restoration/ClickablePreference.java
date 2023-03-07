package com.mydiabetesprt.diabetes.future.preference.restoration;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.mydiabetesprt.diabetes.partage.event.BaseEt;
import com.mydiabetesprt.diabetes.partage.event.ets;

abstract class ClickablePreference <EVENT extends BaseEt> extends Preference implements Preference.OnPreferenceClickListener {

    ClickablePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        ets.post(getEvent());
        return true;
    }

    public abstract EVENT getEvent();
}