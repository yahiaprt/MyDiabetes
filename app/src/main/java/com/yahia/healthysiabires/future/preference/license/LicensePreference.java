package com.yahia.healthysiabires.future.preference.license;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.yahia.healthysiabires.R;/**
 * Created by Filip on 04.11.13.
 */
public class LicensePreference extends Preference {

    public LicensePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected void onClick() {
//        super.onClick();
//        new LicensesDialog.Builder(getContext())
//                .setNotices(R.raw.licenses)
//                .setTitle(R.string.licenses)
//                .setIncludeOwnLicense(true)
//                .build()
//                .show();
//    }
}