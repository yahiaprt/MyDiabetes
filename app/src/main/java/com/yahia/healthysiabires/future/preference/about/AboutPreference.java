package com.yahia.healthysiabires.future.preference.about;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.yahia.healthysiabires.R;public class AboutPreference extends DialogPreference {

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_about);
        setNegativeButtonText(null);


    }
}