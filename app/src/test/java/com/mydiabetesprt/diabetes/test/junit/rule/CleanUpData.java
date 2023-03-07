package com.mydiabetesprt.diabetes.test.junit.rule;

import androidx.test.platform.app.InstrumentationRegistry;

import com.mydiabetesprt.diabetes.partage.data.preference.PreferenceHelper;
import com.mydiabetesprt.diabetes.partage.data.database.Database;

public class CleanUpData extends TestRule {

    @Override
    public void applyBeforeTest() {
        PreferenceHelper.getInstance().init(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @Override
    public void applyAfterTest() {
        Database.getInstance().close();
    }
}
