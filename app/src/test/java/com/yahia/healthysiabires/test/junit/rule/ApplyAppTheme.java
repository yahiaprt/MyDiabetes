package com.yahia.healthysiabires.test.junit.rule;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.yahia.healthysiabires.R;public class ApplyAppTheme extends TestRule {

    @Override
    public void applyBeforeTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.setTheme(R.style.AppTheme);
    }

    @Override
    public void applyAfterTest() {

    }
}
