package com.mydiabetesprt.diabetes.test.junit.rule;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.mydiabetesprt.diabetes.R;public class ApplyAppTheme extends TestRule {

    @Override
    public void applyBeforeTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.setTheme(R.style.AppTheme);
    }

    @Override
    public void applyAfterTest() {

    }
}
