package com.mydiabetesprt.diabetes.future.Entrer.editor.measuration.meal;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mydiabetesprt.diabetes.future.Entrer.editor.EntryEditActivity;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.future.makla.search.maklaSearchActivity;
import com.mydiabetesprt.diabetes.test.junit.rule.CleanUpData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditMeasurementmaklaTest {

    private ActivityScenario<EntryEditActivity> scenario;

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), EntryEditActivity.class);
        intent.putExtra(EntryEditActivity.EXTRA_CATEGORY, type.MEAL);
        scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void clickingmaklaButton_shouldOpenmaklaSearchActivity() {
        scenario.onActivity(activity -> {
            Espresso.onView(ViewMatchers.withText("Add makla"))
                .perform(ViewActions.click());
            Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
            Assert.assertEquals(maklaSearchActivity.class, Shadows.shadowOf(intent).getIntentClass());
        });
    }
}
