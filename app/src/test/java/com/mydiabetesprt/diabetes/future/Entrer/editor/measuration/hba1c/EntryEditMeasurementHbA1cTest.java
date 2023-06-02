package com.mydiabetesprt.diabetes.future.Entrer.editor.measuration.hba1c;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mydiabetesprt.diabetes.future.Entrer.editor.EntryEditActivity;
import com.mydiabetesprt.diabetes.R;
import com.mydiabetesprt.diabetes.partage.data.database.entity.type;
import com.mydiabetesprt.diabetes.test.espresso.matcher.EditTextMatcher;
import com.mydiabetesprt.diabetes.test.junit.rule.CleanUpData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditMeasurementHbA1cTest {

    private ActivityScenario<EntryEditActivity> scenario;

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), EntryEditActivity.class);
        intent.putExtra(EntryEditActivity.EXTRA_CATEGORY, type.HBA1C);
        scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void confirmingEmptyValue_shouldShowWarning() {
        Espresso.onView(ViewMatchers.withId(R.id.fab))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.input))
            .check(ViewAssertions.matches(EditTextMatcher.hasErrorText(R.string.validator_value_empty)));
    }

    @Test
    public void confirmingValueBelowMinimum_shouldShowWarning() {
        Espresso.onView(ViewMatchers.withId(R.id.input))
            .perform(ViewActions.replaceText("0"));
        Espresso.onView(ViewMatchers.withId(R.id.fab))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.input))
            .check(ViewAssertions.matches(EditTextMatcher.hasErrorText(R.string.validator_value_unrealistic)));
    }

    @Test
    public void confirmingValueAboveMaximum_shouldShowWarning() {
        Espresso.onView(ViewMatchers.withId(R.id.input))
            .perform(ViewActions.replaceText("26"));
        Espresso.onView(ViewMatchers.withId(R.id.fab))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.input))
            .check(ViewAssertions.matches(EditTextMatcher.hasErrorText(R.string.validator_value_unrealistic)));
    }

    @Test
    public void confirmingValidValue_shouldFinishActivity() {
        scenario.onActivity(activity -> {
            Espresso.onView(ViewMatchers.withId(R.id.input))
                .perform(ViewActions.replaceText("7"));
            Espresso.onView(ViewMatchers.withId(R.id.fab))
                .perform(ViewActions.click());
            Assert.assertTrue(activity.isFinishing());
        });
    }
}
